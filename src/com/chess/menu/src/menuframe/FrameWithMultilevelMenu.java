package com.chess.menu.src.menuframe;

import com.chess.menu.src.button.ButtonAbstract;
import com.chess.menu.src.buttonpanel.ButtonMultilevelPanelAbstract;

public interface FrameWithMultilevelMenu {
    public void setButtonPanel(ButtonMultilevelPanelAbstract panel);
    public void addToButtonPanel(ButtonAbstract panel, int level);

}
