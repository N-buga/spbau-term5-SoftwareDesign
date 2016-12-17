import tempfile
from unittest import TestCase

import sys

from src.Commands.Wc import Wc
from src.Main import State


class TestWc(TestCase):
    def setUp(self):
        self.state = State()

    def test_wc_one_file(self):
        s = "1 2 3\n4\n5"

        tmp = tempfile.NamedTemporaryFile('w', dir='.')
        tmp.write(s)
        tmp.flush()

        wc = Wc([tmp.name])
        self.assertEqual([tmp.name], wc.arguments)

        wc.execute(self.state)
        self.assertEqual(self.state.variables, {})
        self.assertEqual(self.state.cur_result, "1 5 9" + " " + tmp.name)
