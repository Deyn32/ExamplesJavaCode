// Generated from FZ.g4 by ANTLR 4.7
package ru.nvacenter.bis.npa.antlr;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class FZLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ROMANNUMBERSDOT=1, ARABICNUMBERSBRACKET=2, ARABICNUMBERSDOT=3, ARABICNUMSYMBOL=4, 
		CYRILLICORDINALSDOT=5, HEADERNAMES=6, NEWLINE=7, WHITESPACE=8;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"LOWERCASE", "UPPERCASE", "ARABICNUMBERS", "ROMANNUMBERS", "NUMSYMBOL", 
		"CYRILLICORDINALS", "ROMANNUMBERSDOT", "ARABICNUMBERSBRACKET", "ARABICNUMBERSDOT", 
		"ARABICNUMSYMBOL", "CYRILLICORDINALSDOT", "HEADERNAMES", "NEWLINE", "WHITESPACE", 
		"DOT", "CLOSEBRACKET"
	};

	private static final String[] _LITERAL_NAMES = {
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "ROMANNUMBERSDOT", "ARABICNUMBERSBRACKET", "ARABICNUMBERSDOT", "ARABICNUMSYMBOL", 
		"CYRILLICORDINALSDOT", "HEADERNAMES", "NEWLINE", "WHITESPACE"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public FZLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "FZ.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\n\u010c\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\3\2\3\2"+
		"\3\3\3\3\3\4\3\4\3\4\7\4+\n\4\f\4\16\4.\13\4\3\5\6\5\61\n\5\r\5\16\5\62"+
		"\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7\u00bb\n\7\3\b\3\b\3"+
		"\b\3\t\3\t\3\t\3\n\3\n\7\n\u00c5\n\n\f\n\16\n\u00c8\13\n\3\n\3\n\3\13"+
		"\3\13\5\13\u00ce\n\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r"+
		"\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\r\u00fa\n\r\3\16"+
		"\3\16\6\16\u00fe\n\16\r\16\16\16\u00ff\3\17\6\17\u0103\n\17\r\17\16\17"+
		"\u0104\3\17\3\17\3\20\3\20\3\21\3\21\2\2\22\3\2\5\2\7\2\t\2\13\2\r\2\17"+
		"\3\21\4\23\5\25\6\27\7\31\b\33\t\35\n\37\2!\2\3\2\t\3\2c|\3\2C\\\3\2\63"+
		";\3\2\62;\f\2EEKKNOXXZZeekknoxxzz\4\2\60\60\62;\4\2\13\13\"\"\2\u0122"+
		"\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31"+
		"\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\3#\3\2\2\2\5%\3\2\2\2\7\'\3\2\2\2\t"+
		"\60\3\2\2\2\13\64\3\2\2\2\r\u00ba\3\2\2\2\17\u00bc\3\2\2\2\21\u00bf\3"+
		"\2\2\2\23\u00c2\3\2\2\2\25\u00cb\3\2\2\2\27\u00d1\3\2\2\2\31\u00f9\3\2"+
		"\2\2\33\u00fd\3\2\2\2\35\u0102\3\2\2\2\37\u0108\3\2\2\2!\u010a\3\2\2\2"+
		"#$\t\2\2\2$\4\3\2\2\2%&\t\3\2\2&\6\3\2\2\2\',\t\4\2\2(+\t\5\2\2)+\5\37"+
		"\20\2*(\3\2\2\2*)\3\2\2\2+.\3\2\2\2,*\3\2\2\2,-\3\2\2\2-\b\3\2\2\2.,\3"+
		"\2\2\2/\61\t\6\2\2\60/\3\2\2\2\61\62\3\2\2\2\62\60\3\2\2\2\62\63\3\2\2"+
		"\2\63\n\3\2\2\2\64\65\7P\2\2\65\f\3\2\2\2\66\67\7\u0441\2\2\678\7\u0437"+
		"\2\289\7\u0442\2\29:\7\u0434\2\2:;\7\u0432\2\2;\u00bb\7\u0451\2\2<=\7"+
		"\u0441\2\2=>\7\u0437\2\2>?\7\u0442\2\2?@\7\u0434\2\2@A\7\u044d\2\2A\u00bb"+
		"\7\u043b\2\2BC\7\u0434\2\2CD\7\u0444\2\2DE\7\u0440\2\2EF\7\u0442\2\2F"+
		"G\7\u0432\2\2G\u00bb\7\u0451\2\2HI\7\u0434\2\2IJ\7\u0444\2\2JK\7\u0440"+
		"\2\2KL\7\u0442\2\2LM\7\u0440\2\2M\u00bb\7\u043b\2\2NO\7\u0444\2\2OP\7"+
		"\u0442\2\2PQ\7\u0437\2\2QR\7\u0444\2\2RS\7\u044e\2\2S\u00bb\7\u0451\2"+
		"\2TU\7\u0444\2\2UV\7\u0442\2\2VW\7\u0437\2\2WX\7\u0444\2\2XY\7\u043a\2"+
		"\2Y\u00bb\7\u043b\2\2Z[\7\u0449\2\2[\\\7\u0437\2\2\\]\7\u0444\2\2]^\7"+
		"\u0434\2\2^_\7\u0437\2\2_`\7\u0442\2\2`a\7\u0444\2\2ab\7\u0432\2\2b\u00bb"+
		"\7\u0451\2\2cd\7\u0449\2\2de\7\u0437\2\2ef\7\u0444\2\2fg\7\u0434\2\2g"+
		"h\7\u0437\2\2hi\7\u0442\2\2ij\7\u0444\2\2jk\7\u044d\2\2k\u00bb\7\u043b"+
		"\2\2lm\7\u0441\2\2mn\7\u0451\2\2no\7\u0444\2\2op\7\u0432\2\2p\u00bb\7"+
		"\u0451\2\2qr\7\u0441\2\2rs\7\u0451\2\2st\7\u0444\2\2tu\7\u044d\2\2u\u00bb"+
		"\7\u043b\2\2vw\7\u044a\2\2wx\7\u0437\2\2xy\7\u0443\2\2yz\7\u0444\2\2z"+
		"{\7\u0432\2\2{\u00bb\7\u0451\2\2|}\7\u044a\2\2}~\7\u0437\2\2~\177\7\u0443"+
		"\2\2\177\u0080\7\u0444\2\2\u0080\u0081\7\u0440\2\2\u0081\u00bb\7\u043b"+
		"\2\2\u0082\u0083\7\u0443\2\2\u0083\u0084\7\u0437\2\2\u0084\u0085\7\u0436"+
		"\2\2\u0085\u0086\7\u044e\2\2\u0086\u0087\7\u043e\2\2\u0087\u0088\7\u0432"+
		"\2\2\u0088\u00bb\7\u0451\2\2\u0089\u008a\7\u0443\2\2\u008a\u008b\7\u0437"+
		"\2\2\u008b\u008c\7\u0436\2\2\u008c\u008d\7\u044e\2\2\u008d\u008e\7\u043e"+
		"\2\2\u008e\u008f\7\u0440\2\2\u008f\u00bb\7\u043b\2\2\u0090\u0091\7\u0434"+
		"\2\2\u0091\u0092\7\u0440\2\2\u0092\u0093\7\u0443\2\2\u0093\u0094\7\u044e"+
		"\2\2\u0094\u0095\7\u043e\2\2\u0095\u0096\7\u0432\2\2\u0096\u00bb\7\u0451"+
		"\2\2\u0097\u0098\7\u0434\2\2\u0098\u0099\7\u0440\2\2\u0099\u009a\7\u0443"+
		"\2\2\u009a\u009b\7\u044e\2\2\u009b\u009c\7\u043e\2\2\u009c\u009d\7\u0440"+
		"\2\2\u009d\u00bb\7\u043b\2\2\u009e\u009f\7\u0436\2\2\u009f\u00a0\7\u0437"+
		"\2\2\u00a0\u00a1\7\u0434\2\2\u00a1\u00a2\7\u0451\2\2\u00a2\u00a3\7\u0444"+
		"\2\2\u00a3\u00a4\7\u0432\2\2\u00a4\u00bb\7\u0451\2\2\u00a5\u00a6\7\u0436"+
		"\2\2\u00a6\u00a7\7\u0437\2\2\u00a7\u00a8\7\u0434\2\2\u00a8\u00a9\7\u0451"+
		"\2\2\u00a9\u00aa\7\u0444\2\2\u00aa\u00ab\7\u044d\2\2\u00ab\u00bb\7\u043b"+
		"\2\2\u00ac\u00ad\7\u0436\2\2\u00ad\u00ae\7\u0437\2\2\u00ae\u00af\7\u0443"+
		"\2\2\u00af\u00b0\7\u0451\2\2\u00b0\u00b1\7\u0444\2\2\u00b1\u00b2\7\u0432"+
		"\2\2\u00b2\u00bb\7\u0451\2\2\u00b3\u00b4\7\u0436\2\2\u00b4\u00b5\7\u0437"+
		"\2\2\u00b5\u00b6\7\u0443\2\2\u00b6\u00b7\7\u0451\2\2\u00b7\u00b8\7\u0444"+
		"\2\2\u00b8\u00b9\7\u044d\2\2\u00b9\u00bb\7\u043b\2\2\u00ba\66\3\2\2\2"+
		"\u00ba<\3\2\2\2\u00baB\3\2\2\2\u00baH\3\2\2\2\u00baN\3\2\2\2\u00baT\3"+
		"\2\2\2\u00baZ\3\2\2\2\u00bac\3\2\2\2\u00bal\3\2\2\2\u00baq\3\2\2\2\u00ba"+
		"v\3\2\2\2\u00ba|\3\2\2\2\u00ba\u0082\3\2\2\2\u00ba\u0089\3\2\2\2\u00ba"+
		"\u0090\3\2\2\2\u00ba\u0097\3\2\2\2\u00ba\u009e\3\2\2\2\u00ba\u00a5\3\2"+
		"\2\2\u00ba\u00ac\3\2\2\2\u00ba\u00b3\3\2\2\2\u00bb\16\3\2\2\2\u00bc\u00bd"+
		"\5\t\5\2\u00bd\u00be\5\37\20\2\u00be\20\3\2\2\2\u00bf\u00c0\5\7\4\2\u00c0"+
		"\u00c1\5!\21\2\u00c1\22\3\2\2\2\u00c2\u00c6\t\4\2\2\u00c3\u00c5\t\7\2"+
		"\2\u00c4\u00c3\3\2\2\2\u00c5\u00c8\3\2\2\2\u00c6\u00c4\3\2\2\2\u00c6\u00c7"+
		"\3\2\2\2\u00c7\u00c9\3\2\2\2\u00c8\u00c6\3\2\2\2\u00c9\u00ca\5\37\20\2"+
		"\u00ca\24\3\2\2\2\u00cb\u00cd\5\13\6\2\u00cc\u00ce\7\"\2\2\u00cd\u00cc"+
		"\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00d0\5\7\4\2\u00d0"+
		"\26\3\2\2\2\u00d1\u00d2\5\r\7\2\u00d2\u00d3\5\37\20\2\u00d3\30\3\2\2\2"+
		"\u00d4\u00d5\7\u0415\2\2\u00d5\u00d6\7\u043d\2\2\u00d6\u00d7\7\u0432\2"+
		"\2\u00d7\u00d8\7\u0434\2\2\u00d8\u00fa\7\u0432\2\2\u00d9\u00da\7\u0423"+
		"\2\2\u00da\u00db\7\u0444\2\2\u00db\u00dc\7\u0432\2\2\u00dc\u00dd\7\u0444"+
		"\2\2\u00dd\u00de\7\u044e\2\2\u00de\u00fa\7\u0451\2\2\u00df\u00e0\7\u0422"+
		"\2\2\u00e0\u00e1\7\u0432\2\2\u00e1\u00e2\7\u0439\2\2\u00e2\u00e3\7\u0436"+
		"\2\2\u00e3\u00e4\7\u0437\2\2\u00e4\u00fa\7\u043d\2\2\u00e5\u00e6\7\u0421"+
		"\2\2\u00e6\u00e7\7\u0445\2\2\u00e7\u00e8\7\u043f\2\2\u00e8\u00e9\7\u043c"+
		"\2\2\u00e9\u00fa\7\u0444\2\2\u00ea\u00eb\7\u0421\2\2\u00eb\u00ec\7\u0442"+
		"\2\2\u00ec\u00ed\7\u043a\2\2\u00ed\u00ee\7\u043d\2\2\u00ee\u00ef\7\u0440"+
		"\2\2\u00ef\u00f0\7\u0438\2\2\u00f0\u00f1\7\u0437\2\2\u00f1\u00f2\7\u043f"+
		"\2\2\u00f2\u00f3\7\u043a\2\2\u00f3\u00fa\7\u0437\2\2\u00f4\u00f5\7\u0429"+
		"\2\2\u00f5\u00f6\7\u0432\2\2\u00f6\u00f7\7\u0443\2\2\u00f7\u00f8\7\u0444"+
		"\2\2\u00f8\u00fa\7\u044e\2\2\u00f9\u00d4\3\2\2\2\u00f9\u00d9\3\2\2\2\u00f9"+
		"\u00df\3\2\2\2\u00f9\u00e5\3\2\2\2\u00f9\u00ea\3\2\2\2\u00f9\u00f4\3\2"+
		"\2\2\u00fa\32\3\2\2\2\u00fb\u00fc\7\17\2\2\u00fc\u00fe\7\f\2\2\u00fd\u00fb"+
		"\3\2\2\2\u00fe\u00ff\3\2\2\2\u00ff\u00fd\3\2\2\2\u00ff\u0100\3\2\2\2\u0100"+
		"\34\3\2\2\2\u0101\u0103\t\b\2\2\u0102\u0101\3\2\2\2\u0103\u0104\3\2\2"+
		"\2\u0104\u0102\3\2\2\2\u0104\u0105\3\2\2\2\u0105\u0106\3\2\2\2\u0106\u0107"+
		"\b\17\2\2\u0107\36\3\2\2\2\u0108\u0109\7\60\2\2\u0109 \3\2\2\2\u010a\u010b"+
		"\7+\2\2\u010b\"\3\2\2\2\f\2*,\62\u00ba\u00c6\u00cd\u00f9\u00ff\u0104\3"+
		"\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}