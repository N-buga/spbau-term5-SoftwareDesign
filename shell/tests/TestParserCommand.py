from unittest import TestCase
from src.Commands.Assignment import Assignment
from src.Commands.Cat import Cat
from src.Commands.Echo import Echo
from src.Commands.Executable import Executable
from src.Commands.Exit import Exit
from src.Commands.Pwd import Pwd
from src.Commands.Wc import Wc
from src.ParserCommands import parse_commands


class TestParserCommand(TestCase):
    def test_parse_commands(self):
        line = 'a=2 | wc ../exec.sh | echo 5 | pwd | ../exec.sh 4 | cat ../exec.sh | exit'
        res = parse_commands(line)
        self.assertTrue(isinstance(res[0], Assignment))
        self.assertTrue(isinstance(res[1], Wc))
        self.assertTrue(isinstance(res[2], Echo))
        self.assertTrue(isinstance(res[3], Pwd))
        self.assertTrue(isinstance(res[4], Executable))
        self.assertTrue(isinstance(res[5], Cat))
        self.assertTrue(isinstance(res[6], Exit))
