import os

from src.Commands.Assignment import Assignment
from src.Commands.Cat import Cat
from src.Commands.Executable import Executable
from src.Commands.Exit import Exit
from src.Commands.Pwd import Pwd
from src.Commands.Wc import Wc
from src.ParserUtils import read_argument, read_word, count_of_spaces

from src.Commands.Echo import Echo

# It creates required Command inheritor and return Command as the most common class.


class NameCommands: # KeyWords for definition wich command we need to create
    EXIT = "exit"
    ASSIGNMENT = "assignment"
    EXECUTABLE = "executable"
    CAT = "cat"
    ECHO = "echo"
    PWD = "pwd"
    WC = "wc"
    commands = ["exit", "cat", "echo", "pwd", "wc"] # this is direct command as they in the shell


def create_command(name_command, string_arguments): # name_command - the KeyWord, string_arguments -
    # arguments represented as a string. It's guaranteed, that string_arguments hasn't white spaces at the beginning.
    if name_command == NameCommands.EXIT:
        return Exit()

    if name_command == NameCommands.ASSIGNMENT:
        var = read_word(string_arguments) # read_word - read a word from string, i.e.
                                          # read till the current char is valid(valid chars you can see in Utils).
        # variable can be only a word.
        cur_position = len(var)
        cur_position += count_of_spaces(string_arguments[cur_position:]) # return count of spaces at he beginning
        # of argument string
        if string_arguments[cur_position] != '=':
            # We are supposed to find '=', but if we don't find - we throw exception.
            raise NameError("Unknown command: " + string_arguments)
        cur_position += 1
        val = read_argument(string_arguments[cur_position:]) # read till char is not white space.
        # If there are more then variable and value, we ignore it. It isn't a syntax error.
        return Assignment(var, val)

    if name_command == NameCommands.EXECUTABLE:
        name_script = read_argument(string_arguments) # read till char is not white space
        if os.path.exists(name_script):
            if os.access(name_script, os.X_OK): # if this file is executable
                return Executable(name_script, string_arguments[len(name_script):])
            else:
                raise NameError("File " + '"' + name_script + '"' + " doesn't executable")
        else:
            raise NameError("File " + "'" + name_script + "'" + " doesn't exist")

    if name_command == NameCommands.CAT:
        return Cat(string_arguments.split()) # We suppose that each argument - name of file. It checks in execute

    if name_command == NameCommands.ECHO:
        return Echo(string_arguments) # Echo requires simple a string as argument

    if name_command == NameCommands.PWD:
        return Pwd()   # Pwd doesn't need any arguments

    if name_command == NameCommands.WC:
        return Wc(string_arguments.split()) # We suppose that each argument - name of file. It checks in execute
