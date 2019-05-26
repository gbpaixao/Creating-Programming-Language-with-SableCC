/* This file was generated by SableCC (http://www.sablecc.org/). */

package node;

import analysis.*;

@SuppressWarnings("nls")
public final class TAbreParanteses extends Token
{
    public TAbreParanteses()
    {
        super.setText("(");
    }

    public TAbreParanteses(int line, int pos)
    {
        super.setText("(");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TAbreParanteses(getLine(), getPos());
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTAbreParanteses(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TAbreParanteses text.");
    }
}