package GUI;

import javax.swing.*;
import java.awt.*;

public class BotonPersonalizado extends JButton
{

    public BotonPersonalizado(String texto)
    {
        super(texto);
        inicializarBoton();
    }

    private void inicializarBoton()
    {
        setFont(new Font("Arial", Font.BOLD, 18));
        setForeground(new Color(255, 204, 0)); // Amarillo mostaza
        setContentAreaFilled(false);
        setOpaque(false);
        setBorderPainted(false);
        setFocusPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        int width = getWidth();
        int height = getHeight();

        // Crear un GradientPaint para el difuminado en tonos grises
        GradientPaint gradientPaintLeft = new GradientPaint(0, 0, new Color(128, 128, 128, 0), width / 2, 0, new Color(128, 128, 128, 128));
        GradientPaint gradientPaintRight = new GradientPaint(width / 2, 0, new Color(128, 128, 128, 128), width, 0, new Color(128, 128, 128, 0));

        // Dibujar el primer gradiente (extremo izquierdo al centro)
        g2d.setPaint(gradientPaintLeft);
        g2d.fillRect(0, 0, width / 2, height);

        // Dibujar el segundo gradiente (centro al extremo derecho)
        g2d.setPaint(gradientPaintRight);
        g2d.fillRect(width / 2, 0, width / 2, height);

        g2d.dispose();

        super.paintComponent(g);
    }
    
}
