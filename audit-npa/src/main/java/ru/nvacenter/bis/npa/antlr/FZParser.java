// Generated from FZ.g4 by ANTLR 4.7
package ru.nvacenter.bis.npa.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class FZParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ROMANNUMBERSDOT=1, ARABICNUMBERSBRACKET=2, ARABICNUMBERSDOT=3, ARABICNUMSYMBOL=4, 
		CYRILLICORDINALSDOT=5, HEADERNAMES=6, NEWLINE=7, WHITESPACE=8;
	public static final int
		RULE_doc = 0, RULE_line = 1, RULE_anytextline = 2, RULE_anytext = 3, RULE_numbers = 4, 
		RULE_header = 5, RULE_headerfull = 6, RULE_nl = 7;
	public static final String[] ruleNames = {
		"doc", "line", "anytextline", "anytext", "numbers", "header", "headerfull", 
		"nl"
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

	@Override
	public String getGrammarFileName() { return "FZ.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public FZParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class DocContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(FZParser.EOF, 0); }
		public List<LineContext> line() {
			return getRuleContexts(LineContext.class);
		}
		public LineContext line(int i) {
			return getRuleContext(LineContext.class,i);
		}
		public DocContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_doc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FZListener ) ((FZListener)listener).enterDoc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FZListener ) ((FZListener)listener).exitDoc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FZVisitor ) return ((FZVisitor<? extends T>)visitor).visitDoc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DocContext doc() throws RecognitionException {
		DocContext _localctx = new DocContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_doc);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(19);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(16);
				line();
				}
				}
				setState(21);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(22);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LineContext extends ParserRuleContext {
		public NlContext nl() {
			return getRuleContext(NlContext.class,0);
		}
		public AnytextlineContext anytextline() {
			return getRuleContext(AnytextlineContext.class,0);
		}
		public HeaderfullContext headerfull() {
			return getRuleContext(HeaderfullContext.class,0);
		}
		public LineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_line; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FZListener ) ((FZListener)listener).enterLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FZListener ) ((FZListener)listener).exitLine(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FZVisitor ) return ((FZVisitor<? extends T>)visitor).visitLine(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LineContext line() throws RecognitionException {
		LineContext _localctx = new LineContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_line);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(24);
			nl();
			setState(26);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(25);
				headerfull();
				}
				break;
			}
			setState(28);
			anytextline();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnytextlineContext extends ParserRuleContext {
		public List<AnytextContext> anytext() {
			return getRuleContexts(AnytextContext.class);
		}
		public AnytextContext anytext(int i) {
			return getRuleContext(AnytextContext.class,i);
		}
		public AnytextlineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anytextline; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FZListener ) ((FZListener)listener).enterAnytextline(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FZListener ) ((FZListener)listener).exitAnytextline(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FZVisitor ) return ((FZVisitor<? extends T>)visitor).visitAnytextline(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnytextlineContext anytextline() throws RecognitionException {
		AnytextlineContext _localctx = new AnytextlineContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_anytextline);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(33);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ROMANNUMBERSDOT) | (1L << ARABICNUMBERSBRACKET) | (1L << ARABICNUMBERSDOT) | (1L << ARABICNUMSYMBOL) | (1L << CYRILLICORDINALSDOT) | (1L << HEADERNAMES) | (1L << WHITESPACE))) != 0)) {
				{
				{
				setState(30);
				anytext();
				}
				}
				setState(35);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnytextContext extends ParserRuleContext {
		public TerminalNode NEWLINE() { return getToken(FZParser.NEWLINE, 0); }
		public AnytextContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anytext; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FZListener ) ((FZListener)listener).enterAnytext(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FZListener ) ((FZListener)listener).exitAnytext(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FZVisitor ) return ((FZVisitor<? extends T>)visitor).visitAnytext(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnytextContext anytext() throws RecognitionException {
		AnytextContext _localctx = new AnytextContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_anytext);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(36);
			_la = _input.LA(1);
			if ( _la <= 0 || (_la==NEWLINE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NumbersContext extends ParserRuleContext {
		public TerminalNode ARABICNUMBERSDOT() { return getToken(FZParser.ARABICNUMBERSDOT, 0); }
		public TerminalNode ARABICNUMBERSBRACKET() { return getToken(FZParser.ARABICNUMBERSBRACKET, 0); }
		public TerminalNode ROMANNUMBERSDOT() { return getToken(FZParser.ROMANNUMBERSDOT, 0); }
		public TerminalNode ARABICNUMSYMBOL() { return getToken(FZParser.ARABICNUMSYMBOL, 0); }
		public TerminalNode CYRILLICORDINALSDOT() { return getToken(FZParser.CYRILLICORDINALSDOT, 0); }
		public NumbersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numbers; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FZListener ) ((FZListener)listener).enterNumbers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FZListener ) ((FZListener)listener).exitNumbers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FZVisitor ) return ((FZVisitor<? extends T>)visitor).visitNumbers(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumbersContext numbers() throws RecognitionException {
		NumbersContext _localctx = new NumbersContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_numbers);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(38);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ROMANNUMBERSDOT) | (1L << ARABICNUMBERSBRACKET) | (1L << ARABICNUMBERSDOT) | (1L << ARABICNUMSYMBOL) | (1L << CYRILLICORDINALSDOT))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HeaderContext extends ParserRuleContext {
		public TerminalNode HEADERNAMES() { return getToken(FZParser.HEADERNAMES, 0); }
		public HeaderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_header; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FZListener ) ((FZListener)listener).enterHeader(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FZListener ) ((FZListener)listener).exitHeader(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FZVisitor ) return ((FZVisitor<? extends T>)visitor).visitHeader(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HeaderContext header() throws RecognitionException {
		HeaderContext _localctx = new HeaderContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_header);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40);
			match(HEADERNAMES);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HeaderfullContext extends ParserRuleContext {
		public NumbersContext numbers() {
			return getRuleContext(NumbersContext.class,0);
		}
		public HeaderContext header() {
			return getRuleContext(HeaderContext.class,0);
		}
		public HeaderfullContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_headerfull; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FZListener ) ((FZListener)listener).enterHeaderfull(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FZListener ) ((FZListener)listener).exitHeaderfull(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FZVisitor ) return ((FZVisitor<? extends T>)visitor).visitHeaderfull(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HeaderfullContext headerfull() throws RecognitionException {
		HeaderfullContext _localctx = new HeaderfullContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_headerfull);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==HEADERNAMES) {
				{
				setState(42);
				header();
				}
			}

			setState(45);
			numbers();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NlContext extends ParserRuleContext {
		public TerminalNode NEWLINE() { return getToken(FZParser.NEWLINE, 0); }
		public NlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FZListener ) ((FZListener)listener).enterNl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FZListener ) ((FZListener)listener).exitNl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FZVisitor ) return ((FZVisitor<? extends T>)visitor).visitNl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NlContext nl() throws RecognitionException {
		NlContext _localctx = new NlContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_nl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(47);
			match(NEWLINE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\n\64\4\2\t\2\4\3"+
		"\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\3\2\7\2\24\n\2\f"+
		"\2\16\2\27\13\2\3\2\3\2\3\3\3\3\5\3\35\n\3\3\3\3\3\3\4\7\4\"\n\4\f\4\16"+
		"\4%\13\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\5\b.\n\b\3\b\3\b\3\t\3\t\3\t\2\2"+
		"\n\2\4\6\b\n\f\16\20\2\4\3\2\t\t\3\2\3\7\2/\2\25\3\2\2\2\4\32\3\2\2\2"+
		"\6#\3\2\2\2\b&\3\2\2\2\n(\3\2\2\2\f*\3\2\2\2\16-\3\2\2\2\20\61\3\2\2\2"+
		"\22\24\5\4\3\2\23\22\3\2\2\2\24\27\3\2\2\2\25\23\3\2\2\2\25\26\3\2\2\2"+
		"\26\30\3\2\2\2\27\25\3\2\2\2\30\31\7\2\2\3\31\3\3\2\2\2\32\34\5\20\t\2"+
		"\33\35\5\16\b\2\34\33\3\2\2\2\34\35\3\2\2\2\35\36\3\2\2\2\36\37\5\6\4"+
		"\2\37\5\3\2\2\2 \"\5\b\5\2! \3\2\2\2\"%\3\2\2\2#!\3\2\2\2#$\3\2\2\2$\7"+
		"\3\2\2\2%#\3\2\2\2&\'\n\2\2\2\'\t\3\2\2\2()\t\3\2\2)\13\3\2\2\2*+\7\b"+
		"\2\2+\r\3\2\2\2,.\5\f\7\2-,\3\2\2\2-.\3\2\2\2./\3\2\2\2/\60\5\n\6\2\60"+
		"\17\3\2\2\2\61\62\7\t\2\2\62\21\3\2\2\2\6\25\34#-";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}