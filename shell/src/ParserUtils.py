import re

# Useful things for our script.


valid_characters = "a-zA-Z0-9_-"  # Characters that can be in variables.


def read_word(line):
    return re.search("^[" + valid_characters + "]*", line).group(0)  # Word made of valid characters.


def read_argument(line):  # argument is made of not-space chars.
    cur_position = count_of_spaces(line)
    # We exclude quotes if they are there.
    if line[cur_position] == "'":
        argument = re.search("^[^']*", line[cur_position + 1:]).group(0)
        if len(argument) == len(line):
            raise NameError("Error with quotes")  # If we don't find quote in the end - it's syntaxError
    elif line[cur_position] == '"':
        argument = re.search('^[^"]*', line[cur_position + 1:]).group(0)
        if len(argument) == len(line):
            raise NameError("Error with quotes")  # If we don't find quote in the end - it's syntaxError
    else:
        argument = re.search('^[^\s]*', line[cur_position:]).group(0)
    return argument


def count_of_spaces(line):  # Count of white spaces at the beginning of string.
    len_line = len(line)
    for i in range(len_line):
        if re.match("\s", line[i]):
            continue
        else:
            return i
    return len_line
