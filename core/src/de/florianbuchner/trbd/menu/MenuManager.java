package de.florianbuchner.trbd.menu;

import de.florianbuchner.trbd.core.Resources;

import java.util.LinkedList;

/**
 * Handles multi layered menus
 */
public class MenuManager implements MenuCloseHandler {

    private LinkedList<Menu> menus = new LinkedList <Menu>();

    private Resources resources;

    public MenuManager(Resources resources) {
        this.resources = resources;
    }

    public void clear() {
        this.menus.clear();
    }

    public void add(Menu menu) {
        menu.setMenuCloseHandler(this);
        this.menus.add(menu);
    }

    public void render(float deltaTime) {
        this.resources.spriteBatch.begin();
        for (Menu menu : menus) {
            menu.render(this.resources.spriteBatch, deltaTime);
        }
        this.resources.spriteBatch.end();
    }

    /**
     * Closes the active menu
     */
    @Override
    public void onMenuClosed(Menu menu) {
        this.menus.remove(menu);
    }

    public void closeActive() {
        if (!this.menus.isEmpty()) {
            this.menus.getLast().close();
        }
    }

    public void closeAll() {
        this.menus.clear();
    }

    public boolean menuOpen() {
        return !this.menus.isEmpty();
    }

}
