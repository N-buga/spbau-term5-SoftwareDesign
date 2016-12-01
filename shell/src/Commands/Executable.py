import os
import subprocess

from src.Commands.BaseCommand import Command


# Execute file. It's been checked that file exists and is executable.
from src.ParserUtils import read_argument


class Executable(Command):
    def __init__(self, arguments:str):  # arguments - for executing script
        self.script_name = read_argument(arguments) # read till char is not white space
        self.arguments = arguments[len(self.script_name):]

        if os.path.exists(self.script_name):
            if not os.access(self.script_name, os.X_OK): # if this file is executable
                raise NameError("File " + '"' + self.script_name + '"' + " doesn't executable")
        else:
            raise NameError("File " + "'" + self.script_name + "'" + " doesn't exist")

    def execute(self, shell_state):
        proc = subprocess.Popen([self.script_name, self.arguments], stdout=subprocess.PIPE, stdin=subprocess.PIPE,
                               shell=False) # It tunes subprocess, what it needs to execute, arguments and etc.
        proc.stdin.write(shell_state.cur_result.encode('utf-8'))
        (out, err) = proc.communicate() # Execute and return result and error
#         out = subprocess.check_output([self.script_name, self.arguments],
#                        shell=True, stdin=StringIO.StringIO(shell_state.cur_result))
        shell_state.cur_result = out.decode('utf-8') # Decode result in Python3 string.
        # cur_result in shell_state saves current result to transfer it through pipe or to print.
