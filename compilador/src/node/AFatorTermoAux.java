/* This file was generated by SableCC (http://www.sablecc.org/). */

package node;

import analysis.*;

@SuppressWarnings("nls")
public final class AFatorTermoAux extends PTermoAux
{
    private PFatorAux _fatorAux_;

    public AFatorTermoAux()
    {
        // Constructor
    }

    public AFatorTermoAux(
        @SuppressWarnings("hiding") PFatorAux _fatorAux_)
    {
        // Constructor
        setFatorAux(_fatorAux_);

    }

    @Override
    public Object clone()
    {
        return new AFatorTermoAux(
            cloneNode(this._fatorAux_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFatorTermoAux(this);
    }

    public PFatorAux getFatorAux()
    {
        return this._fatorAux_;
    }

    public void setFatorAux(PFatorAux node)
    {
        if(this._fatorAux_ != null)
        {
            this._fatorAux_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._fatorAux_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._fatorAux_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._fatorAux_ == child)
        {
            this._fatorAux_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._fatorAux_ == oldChild)
        {
            setFatorAux((PFatorAux) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}