// Generated from FZ.g4 by ANTLR 4.7
package ru.nvacenter.bis.npa.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link FZParser}.
 */
public interface FZListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link FZParser#doc}.
	 * @param ctx the parse tree
	 */
	void enterDoc(FZParser.DocContext ctx);
	/**
	 * Exit a parse tree produced by {@link FZParser#doc}.
	 * @param ctx the parse tree
	 */
	void exitDoc(FZParser.DocContext ctx);
	/**
	 * Enter a parse tree produced by {@link FZParser#line}.
	 * @param ctx the parse tree
	 */
	void enterLine(FZParser.LineContext ctx);
	/**
	 * Exit a parse tree produced by {@link FZParser#line}.
	 * @param ctx the parse tree
	 */
	void exitLine(FZParser.LineContext ctx);
	/**
	 * Enter a parse tree produced by {@link FZParser#anytextline}.
	 * @param ctx the parse tree
	 */
	void enterAnytextline(FZParser.AnytextlineContext ctx);
	/**
	 * Exit a parse tree produced by {@link FZParser#anytextline}.
	 * @param ctx the parse tree
	 */
	void exitAnytextline(FZParser.AnytextlineContext ctx);
	/**
	 * Enter a parse tree produced by {@link FZParser#anytext}.
	 * @param ctx the parse tree
	 */
	void enterAnytext(FZParser.AnytextContext ctx);
	/**
	 * Exit a parse tree produced by {@link FZParser#anytext}.
	 * @param ctx the parse tree
	 */
	void exitAnytext(FZParser.AnytextContext ctx);
	/**
	 * Enter a parse tree produced by {@link FZParser#numbers}.
	 * @param ctx the parse tree
	 */
	void enterNumbers(FZParser.NumbersContext ctx);
	/**
	 * Exit a parse tree produced by {@link FZParser#numbers}.
	 * @param ctx the parse tree
	 */
	void exitNumbers(FZParser.NumbersContext ctx);
	/**
	 * Enter a parse tree produced by {@link FZParser#header}.
	 * @param ctx the parse tree
	 */
	void enterHeader(FZParser.HeaderContext ctx);
	/**
	 * Exit a parse tree produced by {@link FZParser#header}.
	 * @param ctx the parse tree
	 */
	void exitHeader(FZParser.HeaderContext ctx);
	/**
	 * Enter a parse tree produced by {@link FZParser#headerfull}.
	 * @param ctx the parse tree
	 */
	void enterHeaderfull(FZParser.HeaderfullContext ctx);
	/**
	 * Exit a parse tree produced by {@link FZParser#headerfull}.
	 * @param ctx the parse tree
	 */
	void exitHeaderfull(FZParser.HeaderfullContext ctx);
	/**
	 * Enter a parse tree produced by {@link FZParser#nl}.
	 * @param ctx the parse tree
	 */
	void enterNl(FZParser.NlContext ctx);
	/**
	 * Exit a parse tree produced by {@link FZParser#nl}.
	 * @param ctx the parse tree
	 */
	void exitNl(FZParser.NlContext ctx);
}