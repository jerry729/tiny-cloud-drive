package filesystem.graphicuserinteraction;

import java.awt.*;

/**
 * @author :frank
 * @date :12:08 2020/12/4
 * @description :TODO
 */
public class GBC extends GridBagConstraints{

    private static final int DEFAULT_WEIGHTX = 100;
    private static  final int DEFAULT_WEIGHTY = 100;

    public GBC(int gridx,int gridy)
    {
        this.gridx = gridx;
        this.gridy = gridy;
        weightx = DEFAULT_WEIGHTX;
        weighty = DEFAULT_WEIGHTY;
    }

    public GBC(int gridx,int gridy,int gridwidth,int gridheight)
    {
        this.gridx = gridx;
        this.gridy = gridy;
        this.gridwidth = gridwidth;
        this.gridheight = gridheight;
    }

    public GBC setAnchor(int anchor)
    {
        this.anchor =anchor;
        return this;
    }

    public GBC setFill(int fill)
    {
        this.fill = fill;
        return this;
    }

    public GBC setWeight(double weightx, double weighty)
    {
        this.weightx = weightx;
        this.weighty = weighty;
        return this;
    }

    public GBC setInsets(int distance)
    {
        this.insets = new Insets(distance,distance,distance,distance);
        return this;
    }

    public GBC setInsets(int top,int left, int bottom, int right)
    {
        this.insets = new Insets(top,left,bottom,right);
        return this;
    }

    public GBC setIpad(int ipadx,int ipady)
    {
        this.ipadx = ipadx;
        this.ipady = ipady;
        return this;
    }
}
