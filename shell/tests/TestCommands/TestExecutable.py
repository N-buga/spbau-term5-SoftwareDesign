import unittest
import tempfile

from src.Commands.Executable import Executable
from src.Main import State


class TestExecutable(unittest.TestCase):
    def setUp(self):
        self.state = State()
        self.variables = {}

    def test_executable_without_args(self):
        result = "a"

        tmp = tempfile.NamedTemporaryFile('w')
        tmp.write("#!/bin/bash\necho " + result)
        file_name = tmp.name

        exec = Executable(file_name, "")
        self.assertEqual(exec.script_name, file_name)
        exec.execute(self.state)
        self.assertEqual(self.variables, self.state.variables)
        self.assertEqual(result, self.state.cur_result)



