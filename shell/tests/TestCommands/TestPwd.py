import os
from unittest import TestCase

from src.Commands.Pwd import Pwd
from src.Main import State


class TestPwd(TestCase):
    def setUp(self):
        self.state = State()

    def test_pwd(self):
        pwd = Pwd()
        pwd.execute(self.state)
        self.assertEqual(self.state.variables, {})
        self.assertEqual(self.state.cur_result, os.getcwd())
