import subprocess

from src.Commands.BaseCommand import Command


# Execute file. It's been checked that file exists and is executable.


class Executable(Command):
    def __init__(self, script_name, arguments):  # arguments - for executing script
        self.script_name = script_name
        self.arguments = arguments

    def execute(self, shell_state):
        proc = subprocess.Popen([self.script_name, self.arguments], stdout=subprocess.PIPE, stdin=subprocess.PIPE,
                               shell=False) # It tunes subprocess, what it needs to execute, arguments and etc.
        proc.stdin.write(shell_state.cur_result.encode('utf-8'))
        (out, err) = proc.communicate() # Execute and return result and error
#         out = subprocess.check_output([self.script_name, self.arguments],
#                        shell=True, stdin=StringIO.StringIO(shell_state.cur_result))
        shell_state.cur_result = out.decode('utf-8') # Decode result in Python3 string.
        # cur_result in shell_state saves current result to transfer it through pipe or to print.
