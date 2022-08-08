// Generated from FZ.g4 by ANTLR 4.7
package ru.nvacenter.bis.npa.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FZParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface FZVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link FZParser#doc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoc(FZParser.DocContext ctx);
	/**
	 * Visit a parse tree produced by {@link FZParser#line}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLine(FZParser.LineContext ctx);
	/**
	 * Visit a parse tree produced by {@link FZParser#anytextline}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnytextline(FZParser.AnytextlineContext ctx);
	/**
	 * Visit a parse tree produced by {@link FZParser#anytext}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnytext(FZParser.AnytextContext ctx);
	/**
	 * Visit a parse tree produced by {@link FZParser#numbers}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumbers(FZParser.NumbersContext ctx);
	/**
	 * Visit a parse tree produced by {@link FZParser#header}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHeader(FZParser.HeaderContext ctx);
	/**
	 * Visit a parse tree produced by {@link FZParser#headerfull}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHeaderfull(FZParser.HeaderfullContext ctx);
	/**
	 * Visit a parse tree produced by {@link FZParser#nl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNl(FZParser.NlContext ctx);
}