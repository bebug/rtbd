package de.florianbuchner.trbd.menu;

import de.florianbuchner.trbd.core.GameData;
import de.florianbuchner.trbd.core.Resources;

public class MainMenu extends AbstractMenu {

    private MainMenuHandler handler;

    public MainMenu(GameData gameData, Resources resources, MainMenuHandler handler) {
        super(120, 40, gameData, resources);
        this.handler = handler;

        this.addMenuButton(new MenuButton("START GAME", -60, -20, 120, 40, new ActionHandler() {
            @Override
            public void doAction() {
                MainMenu.this.setMenuCloseHandler(new MenuCloseHandler() {
                    @Override
                    public void onMenuClosed(Menu menu) {
                        MainMenu.this.handler.onStartGame();
                    }
                });
                MainMenu.this.close();
            }
        }));
    }

    public interface MainMenuHandler {
        void onStartGame();
    }
}
