package uq.deco2800.pyramidscheme.addinfo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;

public class AddInfoMarkert {
    private String cardType;
    private int cardHealth;
    private int cardAttack;
    private String cardDesc;


    private double xOg = 654;
    private double yOg = 74;
    private double wOg = 236;
    private double hOg = 326;
    private Image imgOg = new Image("/actionImages/background.png");

    private double xAtt = 679;
    private double yAtt = 89;
    private double wAtt = 30;
    private double hAtt = 30;
    private Image imgAtt = new Image("/actionImages/attack.png");

    private double xHea = 724;
    private double yHea = 89;
    private double wHea = 30;
    private double hHea = 30;
    private Image imgHea = new Image("/actionImages/health.png");

    private double xDesc = 679;
    private double yDesc = 134;
    private double wDesc = 186;
    private double hDesc = 90;
    private Image imgDesc = new Image("/actionImages/descBackground.jpg");

    private double xAct = 679;
    private double yAct = 239;
    private double wAct = 186;
    private double hAct = 125;
    private Image imgAct;

    public AddInfoMarkert(Card c, double x, double y) {
        this.cardType = c.getType();
        updateVariables(x, y);
        if ("MC".equals(cardType)) {
            this.cardHealth = ((MinionCard) c).getDefense();
            this.cardAttack = ((MinionCard) c).getAttack();
            this.cardDesc = c.getName();
            if (c.getAction() == null) {
                this.imgAct = imgDesc;
            } else {
                this.imgAct = c.getAction().getActionTabImage();
            }
        }
    }

    public void drawPanel(GraphicsContext gc) {
        gc.drawImage(imgOg, xOg, yOg, wOg, hOg);
        drawAttack(gc);
        drawHealth(gc);
        drawDescription(gc);
        drawAction(gc);
    }

    private void updateVariables(double x, double y) {
        this.xOg = x + 6;
        this.yOg = y - 50;
        xAtt = xOg + 25;
        xHea = xOg + 70;
        xDesc = xOg + 25;
        xAct = xOg + 25;
        yAtt = yOg + 15;
        yHea = yOg + 15;
        yDesc = yOg + 60;
        yAct = yOg + 165;
    }

    private void drawAttack(GraphicsContext gc) {
        gc.drawImage(imgAtt, xAtt, yAtt, wAtt, hAtt);
        gc.strokeText(Integer.toString(cardAttack), xAtt + 17, yAtt + 33, (double) 30);
    }

    private void drawHealth(GraphicsContext gc) {
        gc.drawImage(imgHea, xHea, yHea, wHea, hHea);
        gc.strokeText(Integer.toString(cardHealth), xHea + 22, yHea + 33, (double) 30);
    }

    private void drawDescription(GraphicsContext gc) {
        gc.drawImage(imgDesc, xDesc, yDesc, wDesc, hDesc);
        gc.strokeText(cardDesc, xDesc + 80, yDesc + 19, wDesc);
    }

    private void drawAction(GraphicsContext gc) {
        gc.drawImage(imgAct, xAct, yAct, wAct, hAct);
    }
}
