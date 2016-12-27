from src.ParserSubst import *
from src.Replacer import *

from src.ParserCommands import *


# Main parse function. It manage the process.


def parse(line, shell_status):
    substitution_positions = find_substitute_positions(line) # Find list of positions of variables.
    new_line = substitute(line, substitution_positions, shell_status) # Replace them on values
    return parse_commands(new_line) # Parse commands and return list of commands.
