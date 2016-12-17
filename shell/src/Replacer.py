from src.ParserUtils import read_word

# We get list of beginning variables, and replace it to their values


def substitute(line, positions_for_replace, shell_status):
    new_line = [] # That will be our new string in the end
    len_line = len(line)
    position = 0
    while position < len_line:
        if position in positions_for_replace:
            position += 1 # We need to skip &
            var = read_word(line[position:])
            value = shell_status.find_variable(var)
            if value is None: # If we can't find variable in env, we return null and raise Error!
                raise NameError("Not found variable: " + var)
            # If all is right, we add value in new_line.
            new_line.append(value)
            position += len(var)
        else:
            # We add also not variable-value characters to new string.
            new_line.append(line[position])
            position += 1
    return "".join(new_line)
