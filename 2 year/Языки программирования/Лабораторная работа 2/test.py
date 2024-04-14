from subprocess import Popen, PIPE

inputs = ["first word", "second word", "third word", "ahaha", "L" * 256, "L" * 257]
outputs = ["first word explanation", "second word explanation", "third word explanation", "", "L word explanation", ""]
errors = ["", "", "", "That label doesn`t exists", "", "The buffer is overflowed. Max - 255 symbols"]
exit_codes = [0, 0, 0, 1, 0, 1]

script_path = "./main"
test_output = ""

print("TESTING")
print(f"script: {script_path} ")
print("summary: ", end="")
for i in range(len(inputs)):
    process = Popen([script_path], stdin=PIPE, stdout=PIPE, stderr=PIPE)
    data = process.communicate(inputs[i].encode())
    if data[0].decode().strip() == outputs[i] and data[1].decode().strip() == errors[i] and process.returncode == exit_codes[i]:
        print('.', end="")
        test_output += "----------------------\n"
        test_output += f"TEST {i} PASSED\n"
        test_output += f"STDOUT: {inputs[i]}\n"
    else:
        print('F', end="")
        test_output += "----------------------\n"
        test_output += f"TEST {i} FAILED\n"
        test_output += f"OUTPUT STDOUT: {data[0].decode().strip()}\n"
        test_output += f"OUTPUT STDERR: {data[1].decode().strip()}\n"
        test_output += f"OUTPUT EXIT CODE: {process.returncode}\n"
        test_output += f"OUTPUT EXPECTED STDOUT: {outputs[i]}\n"
        test_output += f"OUTPUT EXPECTED STDERR: {errors[i]}\n"
        test_output += f"OUTPUT EXPECTED EXIT CODE: {exit_codes[i]}\n"
test_output += "----------------------\n"
print()
print(test_output)        

