import os

from src.Commands.BaseCommand import Command
import types

# Prints file content.


class Cat(Command):
    def __init__(self, arguments):
        if isinstance(arguments, type([])):
            self.arguments = arguments
        else:
            self.arguments = [arguments]

    def execute(self, shell_state):
        shell_state.cur_result = ""
        if not self.arguments:  # If self.arguments is null then we do nothing and return immediately.
            return
        for argument in self.arguments:  # Else we sort out all argument as file and try to show their inside.
            if not os.path.exists(argument):
                raise NameError("File " + "'" + argument + "'" + " doesn't find")   # Something goes wrong. Very bad.
                                                    # We raise Exception to handle it in Main.
            file_strings = [] # It will contain all strings of file
            try:
                with open(argument, 'r') as fin:  # We open file in with so it closes automatically in the end of cycle.
                    file_strings.append(fin.read())
            except IsADirectoryError as e:
                raise NameError('File ' + "'" + argument + "'" + " is a directory.")
            shell_state.cur_result += '\n'.join(file_strings) + '\n'    # This line unions all strings together.
            # cur_result in shell_state saves current result to transfer it through pipe or to print.
