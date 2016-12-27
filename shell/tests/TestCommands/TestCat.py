import tempfile
import unittest

from src.Commands.Cat import Cat
from src.Main import State


class TestCat(unittest.TestCase):
    def setUp(self):
        self.state = State()
        self.variables = {}

    def test_cat(self):
        content1 = "a"
        content2 = "b"

        test_f = [tempfile.NamedTemporaryFile(mode = 'w'), tempfile.NamedTemporaryFile(mode = 'w')]

        s1 = test_f[0].name
        s2 = test_f[1].name

        test_f[0].write(content1)
        test_f[0].flush()
        test_f[1].write(content2)
        test_f[1].flush()

        c = [Cat(s1), Cat([s1, s2])]

        self.assertEqual([s1], c[0].arguments)
        self.assertEqual([s1, s2], c[1].arguments)

        c[0].execute(self.state)
        self.assertEqual(self.variables, self.state.variables)
        self.assertEqual(content1, self.state.cur_result)

        c[1].execute(self.state)
        self.assertEqual(self.variables, self.state.variables)
        self.assertEqual(content1 + '\n' + content2, self.state.cur_result)

        for fp in test_f:
            fp.close()
