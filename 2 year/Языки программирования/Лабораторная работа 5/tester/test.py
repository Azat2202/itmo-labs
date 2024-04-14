from subprocess import Popen, PIPE

image_dir = "./images/"
test_filenames = ["cat.bmp", "door.bmp", "input.bmp"]
test_count = 10
script_path = "./obj/main"
test_output = ""

print("TESTING")
print(f"script: {script_path} ")
print("summary: ", end="")
for i in range(len(test_filenames)):
    print(f"---------------TEST {i}-------------")
    input_file = image_dir + test_filenames[i]
    output_file = image_dir + test_filenames[i].split(".")[0] + "_out.bmp"
    process = Popen([script_path, input_file, output_file, str(test_count)], stdout=PIPE, stderr=PIPE)
    data = process.communicate()
    stdout_data = data[0].decode();
    stderr_data = data[1].decode();
    if process.returncode != 0:
        print(f"Error while performing test {i} on file {input_file}")
        print("Got in stdout:")
        print(stdout_data)
        print("Got in stderr:")
        print(stderr_data)
        continue
    c_time = float(stdout_data.split("\n")[2].split()[-2])
    asm_time = float(stdout_data.split("\n")[3].split()[-2])
    if asm_time / c_time <= 0.5:
        print(f"Test passed! Speeding up by {int(100 - asm_time / c_time * 100)}%")
    elif asm_time / c_time <= 0.75:
        print(f"Warning! Speeding up is little:")
        print(f"C time: {c_time} seconds")
        print(f"Asm time: {asm_time} seconds")
    else:
        print(f"Error! Speeding up is not enough:")
        print(f"C time: {c_time} seconds")
        print(f"Asm time: {asm_time} seconds")

