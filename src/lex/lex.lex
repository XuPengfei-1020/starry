// 正则表达式本身的文法定义
no_display: \f
          | \n
          | \r
          | \s
          | \S
          | \t

remains  : .

signal_op
        : *

double _op
        : \|
        ;

keywords: remains
        | no_display
        | op

times   : {n}
        | {n, m}
        | {n?, m}
        | {n, m?}

group  : [character-character]
       | [factor]

anti_group  : [^character-character]
       | [^factor]

stmt  : character
        | group
        | (stmt)
        | stmt single_op stmt
        | stmt double_op
        | stmt times
        | stmt stmt*