package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PanelBase extends JPanel
{

    // CONSTRUCTOR
    public PanelBase()
    {
        super();
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        int width = getWidth();
        int height = getHeight();

        // Crear un BufferedImage para el degradado
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gbi = bufferedImage.createGraphics();

        // Crear un degradado diagonal del verde oscuro al negro, con un punto medio
        GradientPaint gradientPaint = new GradientPaint(0, 0, new Color(0, 100, 0), width * 3 / 4, height * 3 / 4, Color.BLACK);
        gbi.setPaint(gradientPaint);
        gbi.fillRect(0, 0, width, height);
        gbi.dispose();

        // Usar TexturePaint para pintar el fondo con el BufferedImage
        Rectangle rect = new Rectangle(0, 0, width, height);
        TexturePaint texturePaint = new TexturePaint(bufferedImage, rect);
        g2d.setPaint(texturePaint);
        g2d.fillRect(0, 0, width, height);

        g2d.dispose();
    }
}
