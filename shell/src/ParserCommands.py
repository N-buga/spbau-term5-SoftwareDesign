import re

from src.ParserUtils import count_of_spaces, read_word, valid_characters

from src.CommandsFactory import NameCommands, create_command


# Parse all commands from string.


def parse_commands(line):
    string_commands = line.split('|')
    commands = [] # it will be a list of commands that's in line
    for string_command in string_commands:  # Try to define for each command between pipe.
        if string_command == "":
            raise NameError("There're no command between, after or before pipe")
        begin_command = count_of_spaces(string_command) # Skip all whiteSpaces at the beginning.
        if re.match("[" + valid_characters + "]", string_command[begin_command]):
            # If there are a word(i.e. one or more valid chars - valid chars u can see in Utils) at the beginning:
            first_word = read_word(string_command[begin_command:])
            if first_word in NameCommands.commands: # If this word is direct Command - we define a command!
                name_command = first_word
                arg_position = begin_command + len(name_command)
                arg_position += count_of_spaces(string_command[arg_position:])
                # now there're no white space at the beginning of the argument string.
                cur_command = create_command(name_command, string_command[arg_position:])
            elif len(first_word) < len(string_command) and string_command[len(first_word)] == '=':
                # If assign goes after our word, then it is assignment.
                cur_command = create_command(NameCommands.ASSIGNMENT,
                                             string_command[begin_command:])
            else:
                # The one type that left is Executable. Try to define it as executable.
                cur_command = create_command(NameCommands.EXECUTABLE,
                                             string_command[begin_command:])
        else:
            # The one type that left is Executable. Try to define it as executable.
            cur_command = create_command(NameCommands.EXECUTABLE,
                                         string_command[begin_command:])
        commands.append(cur_command)
    return commands
