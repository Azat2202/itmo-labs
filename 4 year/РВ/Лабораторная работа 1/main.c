#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>
#include <time.h>

#include "ipc.h"
#include "common.h"
#include "pa1.h"

typedef struct
{
    local_id id;
    int num_processes;
    int pipes[MAX_PROCESS_ID + 1][MAX_PROCESS_ID + 1][2];
    FILE *events_log_f;
} ProcessContext;

void setup_pipes(ProcessContext *ctx);
void close_unused_pipes(ProcessContext *ctx);
void log_event(ProcessContext *ctx, const char *msg);
int run_child_process(ProcessContext *ctx);
int run_parent_process(ProcessContext *ctx);

int main(int argc, char *argv[])
{
    int num_children = 0;
    if (argc == 3 && strcmp(argv[1], "-p") == 0)
    {
        num_children = atoi(argv[2]);
        if (num_children <= 0 || num_children > MAX_PROCESS_ID)
        {
            fprintf(stderr, "Number of processes: %d must be between 1 and %d.\n", num_children, MAX_PROCESS_ID);
            return 1;
        }
    }
    else
    {
        fprintf(stderr, "Usage: %s -p <number_of_processes>\n", argv[0]);
        return 1;
    }

    int num_processes = num_children + 1;
    ProcessContext ctx;
    ctx.num_processes = num_processes;

    ctx.events_log_f = fopen(events_log, "w");
    if (!ctx.events_log_f)
    {
        perror("fopen events.log");
        return 1;
    }

    setup_pipes(&ctx);

    pid_t pids[num_children];
    for (int i = 1; i <= num_children; i++)
    {
        pids[i - 1] = fork();
        if (pids[i - 1] == -1)
        {
            perror("fork");
            return 1;
        }
        else if (pids[i - 1] == 0)
        {
            ctx.id = i;
            close_unused_pipes(&ctx);
            int result = run_child_process(&ctx);
            fclose(ctx.events_log_f);
            exit(result);
        }
    }

    ctx.id = PARENT_ID;
    close_unused_pipes(&ctx);
    int result = run_parent_process(&ctx);

    for (int i = 0; i < num_children; i++)
    {
        wait(NULL);
    }

    fclose(ctx.events_log_f);
    return result;
}

void setup_pipes(ProcessContext* ctx) {
    FILE* pipes_log_f = fopen(pipes_log, "w");
    if (!pipes_log_f) {
        perror("fopen pipes.log");
        exit(1);
    }

    for (int i = 0; i < ctx->num_processes; i++) {
        for (int j = 0; j < ctx->num_processes; j++) {
            if (i != j) {
                if (pipe(ctx->pipes[i][j]) == -1) {
                    perror("pipe");
                    exit(1);
                }
                fprintf(pipes_log_f, "Pipe from %d to %d: read_fd=%d, write_fd=%d\n", i, j, ctx->pipes[i][j][0], ctx->pipes[i][j][1]);
            }
        }
    }
    fclose(pipes_log_f);
}

void close_unused_pipes(ProcessContext *ctx)
{
    for (int i = 0; i < ctx->num_processes; i++)
    {
        for (int j = 0; j < ctx->num_processes; j++)
        {
            if (i == j)
                continue;
            if (i == ctx->id)
            {                           
                close(ctx->pipes[i][j][0]);
            }
            else if (j == ctx->id)
            {                               
                close(ctx->pipes[i][j][1]);
            }
            else
            { 
                close(ctx->pipes[i][j][0]);
                close(ctx->pipes[i][j][1]);
            }
        }
    }
}

void log_event(ProcessContext *ctx, const char *msg)
{
    printf("%s", msg);
    fprintf(ctx->events_log_f, "%s", msg);
    fflush(ctx->events_log_f);
}

int run_child_process(ProcessContext *ctx)
{
    // Prepare
    char buf[MAX_PAYLOAD_LEN];

    sprintf(buf, log_started_fmt, ctx->id, getpid(), getppid());
    log_event(ctx, buf);

    Message msg;
    msg.s_header.s_magic = MESSAGE_MAGIC;
    msg.s_header.s_type = STARTED;
    msg.s_header.s_local_time = time(NULL);
    msg.s_header.s_payload_len = strlen(buf);
    memcpy(msg.s_payload, buf, msg.s_header.s_payload_len);

    if (send_multicast(ctx, &msg) != 0)
    {
        fprintf(stderr, "Child %d: send_multicast(STARTED) failed\n", ctx->id);
        return 1;
    }

    for (int i = 1; i < ctx->num_processes; i++)
    {
        if (i == ctx->id)
            continue;
        if (receive(ctx, i, &msg) != 0 || msg.s_header.s_type != STARTED)
        {
            fprintf(stderr, "Child %d: receive(STARTED from %d) failed\n", ctx->id, i);
            return 1;
        }
    }
    sprintf(buf, log_received_all_started_fmt, ctx->id);
    log_event(ctx, buf);

    // Work

    // Done
    sprintf(buf, log_done_fmt, ctx->id);
    log_event(ctx, buf);

    msg.s_header.s_type = DONE;
    msg.s_header.s_local_time = time(NULL);
    msg.s_header.s_payload_len = strlen(buf);
    memcpy(msg.s_payload, buf, msg.s_header.s_payload_len);

    if (send_multicast(ctx, &msg) != 0)
    {
        fprintf(stderr, "Child %d: send_multicast(DONE) failed\n", ctx->id);
        return 1;
    }

    // Receive all done messages
    for (int i = 1; i < ctx->num_processes; i++)
    {
        if (i == ctx->id)
            continue;
        if (receive(ctx, i, &msg) != 0 || msg.s_header.s_type != DONE)
        {
            fprintf(stderr, "Child %d: receive(DONE from %d) failed\n", ctx->id, i);
            return 1;
        }
    }
    sprintf(buf, log_received_all_done_fmt, ctx->id);
    log_event(ctx, buf);

    return 0;
}

int run_parent_process(ProcessContext *ctx)
{
    Message msg;

    // Receive all started messages
    for (int i = 1; i < ctx->num_processes; i++)
    {
        if (receive(ctx, i, &msg) != 0 || msg.s_header.s_type != STARTED)
        {
            fprintf(stderr, "Parent: receive(STARTED from %d) failed\n", i);
            return 1;
        }
    }
    char buf[MAX_PAYLOAD_LEN];
    sprintf(buf, log_received_all_started_fmt, ctx->id);
    log_event(ctx, buf);

    // Receive all done messages
    for (int i = 1; i < ctx->num_processes; i++)
    {
        if (receive(ctx, i, &msg) != 0 || msg.s_header.s_type != DONE)
        {
            fprintf(stderr, "Parent: receive(DONE from %d) failed\n", i);
            return 1;
        }
    }
    sprintf(buf, log_received_all_done_fmt, ctx->id);
    log_event(ctx, buf);

    return 0;
}
