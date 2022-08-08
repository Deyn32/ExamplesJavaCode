grammar FZ;

/*
 * Parser Rules
 */

doc             : line* EOF ;
line			: nl headerfull? anytextline;
anytextline		: anytext* ;
anytext			: ~NEWLINE;
numbers			: ARABICNUMBERSDOT | ARABICNUMBERSBRACKET | ROMANNUMBERSDOT | ARABICNUMSYMBOL | CYRILLICORDINALSDOT;
header			: HEADERNAMES;
headerfull		: header? numbers;
nl :NEWLINE;


/*
 * Lexer Rules
 */

fragment LOWERCASE  : [a-z] ;
fragment UPPERCASE  : [A-Z] ;
fragment ARABICNUMBERS       : [1-9]([0-9] | DOT)* ;
fragment ROMANNUMBERS        : ('I' | 'V' | 'M' | 'X' | 'C' | 'L' | 'i' | 'v' | 'm' | 'x' | 'c' | 'l')+;
fragment NUMSYMBOL        : 'N';
fragment CYRILLICORDINALS	: 'первая' | 'первый' | 'вторая' | 'второй' | 'третья' | 'третий' | 'четвертая' | 'четвертый' | 'пятая' | 'пятый' | 'шестая' | 'шестой' | 'седьмая' | 'седьмой' | 'восьмая' | 'восьмой' | 'девятая' | 'девятый' | 'десятая' | 'десятый';
 ROMANNUMBERSDOT        :  ROMANNUMBERS DOT;
 ARABICNUMBERSBRACKET	:  ARABICNUMBERS CLOSEBRACKET;
 ARABICNUMBERSDOT	: [1-9][0-9.]* DOT;
 ARABICNUMSYMBOL	: NUMSYMBOL ' '? ARABICNUMBERS;
 CYRILLICORDINALSDOT :  CYRILLICORDINALS DOT;

HEADERNAMES			: 'Глава' | 'Статья' | 'Раздел' | 'Пункт' | 'Приложение' | 'Часть';

NEWLINE             : ('\r\n')+ ;

WHITESPACE          : (' '|'\t')+ -> skip ;
/*
NUMBERS				: ARABICNUMBERSDOT | ARABICNUMBERSBRACKET | ROMANNUMBERSDOT;
*/
fragment DOT					: '.';
fragment CLOSEBRACKET				: ')';
