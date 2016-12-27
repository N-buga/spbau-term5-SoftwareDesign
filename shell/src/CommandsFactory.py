import os

from src.Commands.Assignment import Assignment
from src.Commands.Cat import Cat
from src.Commands.Executable import Executable
from src.Commands.Exit import Exit
from src.Commands.Grep import Grep
from src.Commands.Pwd import Pwd
from src.Commands.Wc import Wc
from src.Commands.Cd import Cd
from src.Commands.Ls import Ls
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
    GREP = "grep"
    CD = "cd"
    LS = "ls"
    commands = ["exit", "cat", "echo", "pwd", "wc", "grep", "cd", "ls"] # this is direct command as they in the shell


def create_command(name_command, string_arguments): # name_command - the KeyWord, string_arguments -
    # arguments represented as a string. It's guaranteed, that string_arguments hasn't white spaces at the beginning.
    if name_command == NameCommands.EXIT:
        return Exit()

    if name_command == NameCommands.ASSIGNMENT:
        return Assignment(string_arguments)

    if name_command == NameCommands.EXECUTABLE:
        return Executable(string_arguments)

    if name_command == NameCommands.CAT:
        return Cat(string_arguments.split()) # We suppose that each argument - name of file. It checks in execute

    if name_command == NameCommands.ECHO:
        return Echo(string_arguments) # Echo requires simple a string as argument

    if name_command == NameCommands.PWD:
        return Pwd()   # Pwd doesn't need any arguments

    if name_command == NameCommands.WC:
        return Wc(string_arguments.split()) # We suppose that each argument - name of file. It checks in execute

    if name_command == NameCommands.GREP:
        return Grep(string_arguments)

    if name_command == NameCommands.CD:
        return Cd(string_arguments)

    if name_command == NameCommands.LS:
        return Ls()