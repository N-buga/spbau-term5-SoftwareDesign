import re

from src import ParserUtils


# Here we find variables in line and return their positions

def find_substitute_positions(line):
    line_len = len(line)
    positions = []
    is_una_quotes = False # In una quotes we don't need to replace variables, so they won't in our list
    for position in range(line_len):
        if line[position] == '\'':
            is_una_quotes ^= True
        if line[position] == '$' and not is_una_quotes:
            if position + 1 < line_len and re.match("[" + ParserUtils.valid_characters + "]",
                                                    line[position + 1]):
                positions.append(position)
                # If after & at least one valid char, then it can be a var, so we add it in list.
    return positions
