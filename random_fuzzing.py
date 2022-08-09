import subprocess
import sys
import random
import string
from tqdm import tqdm
import time
from datetime import datetime

def random_fuzz():
    random.seed(datetime.now())
    string_length = random.randint(0,1024)
    input1 = ""
    input2 = ""
    input3 = ""
    for i in range(string_length):
        input1 += string.printable[random.randint(0,99)]
    for i in range(string_length):
        input2 += string.printable[random.randint(0,99)]
    for i in range(string_length):
        input3 += string.printable[random.randint(0,99)]
    # print("input1: " + input1)
    # print("input2: " + input2)
    # print("input3: " + input3)
    
    
    try:
        popen = subprocess.Popen(['java', 'App', input1, input2, input3], stdout=subprocess.PIPE, universal_newlines=True)
        for stdout_line in iter(popen.stdout.readline, ""):
            yield stdout_line
        popen.stdout.close()
    except subprocess.CalledProcessError as e:
        exit_code = e.returncode
        stderror = e.stderr
        print(exit_code, stderror)
        

if __name__ == "__main__":
    iterations = 100
    fail = False
    for i in tqdm(range(iterations)):
        outputs = random_fuzz()
        for output in outputs:
            if output == None:
                print("Random Fuzzing Failed")
                fail = True
                break
        if fail == True:
            break
    if fail == False:
        print("Random Fuzzing Succeeded, No Bugs Detected")
    