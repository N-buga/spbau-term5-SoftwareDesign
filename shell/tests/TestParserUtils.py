from unittest import TestCase
from src.ParserUtils import *


class TestParserUtils(TestCase):

    def test_read_word(self):
        actual_res = '123Top456'
        s = actual_res + ' is the name of this robot.'
        res = read_word(s)
        self.assertEqual(res, actual_res)

    def test_read_argument(self):
        actual_res = 'sskn46765^^^43]]'
        s = actual_res + ' is the'
        res = read_argument(s)
        self.assertEqual(res, actual_res)

    def test_count_of_spaces(self):
        actual_res = 5
        s = ''
        for i in range(actual_res):
            s += ' '
        res1 = count_of_spaces(s)
        s += 'slfks;k  ff'
        res2 = count_of_spaces(s)
        self.assertEqual(actual_res, res1)
        self.assertEqual(actual_res, res2)

    def test_delete_quotes(self):
        s = "AAA'B\"C'DD\"A'''BD\"Q"
        actual_res = "AAAB\"CDDA'''BDQ"
        res = delete_quotes(s)
        self.assertEqual(res, actual_res)

