import os
import stat
import unittest
import tempfile

from src.Commands.Executable import Executable
from src.Main import State


class TestExecutable(unittest.TestCase):
    def setUp(self):
        self.state = State()
        self.variables = {}

    def create_tmp(self, contains):
        tmp = tempfile.NamedTemporaryFile('w', dir='.')
        tmp.write(contains)
        tmp.flush()
        os.chmod(tmp.name, os.stat(tmp.name).st_mode | stat.S_IEXEC)
        tmp.file.close()
        return tmp

    def check_and_execute(self, exec, tmp, result):
        self.assertEqual(exec.script_name, tmp.name)
        exec.execute(self.state)
        self.assertEqual(self.variables, self.state.variables)
        self.assertEqual(result + '\n', self.state.cur_result)

    def test_executable_without_args(self):
        result = "a"

        tmp = self.create_tmp("#!/bin/bash\necho " + '"' + result + '"')

        try:
            exec = Executable(tmp.name, "")
            self.check_and_execute(exec, tmp, result)
        finally:
            tmp.close()

    def test_executable_with_args(self):
        result = "a"

        tmp = self.create_tmp("#!/bin/bash\necho $1")
        try:
            exec = Executable(tmp.name, result)
            self.check_and_execute(exec, tmp, result)
        finally:
            tmp.close()

