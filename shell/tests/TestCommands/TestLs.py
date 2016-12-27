import unittest

from src.Commands.Ls import Ls
from src.Main import State


class TestLs(unittest.TestCase):
    def setUp(self):
        self.state = State()

    def test_ls(self):
        ls = Ls()
        ls.execute(self.state)
        self.assertEqual(self.state.variables, {})
        self.state.cur_result = "{}".format(self.state.cur_result)
        self.assertEqual(self.state.cur_result, '[\'TestAssignment.py\', \'TestCat.py\', \'TestCd.py\', '
                                                '\'TestEcho.py\', \'TestExecutable.py\', \'TestGrep.py\', '
                                                '\'TestLs.py\', \'TestPwd.py\', \'TestWc.py\', \'__init__.py\']')
