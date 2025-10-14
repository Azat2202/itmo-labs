#include <unistd.h>
#include <string.h>
#include <stdio.h>

#include "ipc.h"

// Define ProcessContext structure, assuming it's defined in the main file
// This is a bit of a hack, ideally this would be in a shared header.
typedef struct {
    local_id id;
    int num_processes;
    int pipes[MAX_PROCESS_ID + 1][MAX_PROCESS_ID + 1][2];
    FILE* events_log_f;
} ProcessContext;


int send(void * self, local_id dst, const Message * msg) {
    ProcessContext* ctx = (ProcessContext*) self;
    local_id src = ctx->id;
    int write_fd = ctx->pipes[src][dst][1];
    
    if (write(write_fd, &msg->s_header, sizeof(MessageHeader)) != sizeof(MessageHeader)) {
        perror("send header");
        return -1;
    }
    if (msg->s_header.s_payload_len > 0) {
        if (write(write_fd, msg->s_payload, msg->s_header.s_payload_len) != msg->s_header.s_payload_len) {
            perror("send payload");
            return -1;
        }
    }
    return 0;
}

int send_multicast(void * self, const Message * msg) {
    ProcessContext* ctx = (ProcessContext*) self;
    for (local_id i = 0; i < ctx->num_processes; i++) {
        if (i != ctx->id) {
            if (send(self, i, msg) != 0) {
                return -1;
            }
        }
    }
    return 0;
}

int receive(void * self, local_id from, Message * msg) {
    ProcessContext* ctx = (ProcessContext*) self;
    local_id dst = ctx->id;
    int read_fd = ctx->pipes[from][dst][0];

    if (read(read_fd, &msg->s_header, sizeof(MessageHeader)) != sizeof(MessageHeader)) {
        // Could be a legitimate EOF if the writer closed the pipe
        return -1; 
    }

    if (msg->s_header.s_magic != MESSAGE_MAGIC) {
        fprintf(stderr, "Process %d received a message with wrong magic number from %d\n", dst, from);
        return -1;
    }

    if (msg->s_header.s_payload_len > 0) {
        if (read(read_fd, msg->s_payload, msg->s_header.s_payload_len) != msg->s_header.s_payload_len) {
            perror("receive payload");
            return -1;
        }
    }
    return 0;
}

int receive_any(void * self, Message * msg) {
    // This is a simplified and potentially blocking implementation.
    // A real implementation would need non-blocking I/O and a mechanism like select() or poll(),
    // but the assignment forbids them.
    // This implementation will just loop and block on the first available pipe.
    ProcessContext* ctx = (ProcessContext*) self;
    while (1) {
        for (local_id i = 0; i < ctx->num_processes; i++) {
            if (i != ctx->id) {
                // Try to receive from process i.
                // This will block until a message is available from process i.
                if (receive(self, i, msg) == 0) {
                    return 0; // Message received
                }
            }
        }
    }
    return -1; // Should not be reached
}
