val input = """Record: 1
P02: "another sample text"
P01: -321
%
"""

import alaska.parser
import alaska.parser.RecordParser


println(input)

val res = RecordParser.parseRecord(input)

println(res)

println(RecordParser.fields.parse("P02: \"another sample text\"\n"))

RecordParser.fields.parseAll("""P02: "another sample text"
P01: -321
""")
