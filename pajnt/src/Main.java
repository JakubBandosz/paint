import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Paint");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Canvas canvas = new Canvas();

        // Przycisk wyboru koloru
        JButton colorButton = new JButton("Kolor");
        JButton clearButton = new JButton("Wyczyść");
        JButton rubberButton = new JButton("Gumka");

        colorButton.addActionListener(e -> {

            Color chosen = JColorChooser.showDialog(
                    frame,
                    "Wybierz kolor",
                    canvas.getCurrentColor()
            );

            if (chosen != null) {
                canvas.setCurrentColor(chosen);
            }
        });

        clearButton.addActionListener(e ->{
            canvas.clear();
                });
        rubberButton.addActionListener( e -> {
            canvas.rubber();
        });

        // Panel na przyciski
        JPanel tools = new JPanel();
        tools.add(colorButton);
        tools.add(clearButton);
        tools.add(rubberButton);

        frame.add(tools, BorderLayout.NORTH);
        frame.add(canvas, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}


class Canvas extends JPanel {
    private boolean isRubber;
    private boolean isDrawing;

    // Wszystkie kreski
    private ArrayList<Stroke> strokes = new ArrayList<>();

    // Kreska, którą aktualnie rysujemy
    private Stroke currentStroke;

    // Aktualnie wybrany kolor
    private Color currentColor = Color.BLACK;


    Canvas() {
        setBackground(Color.WHITE);
        // Wciśnięcie i puszczenie myszy
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                isDrawing = true;

                if(isRubber){
                    // Tworzymy nową kreskę z aktualnym kolorem
                    currentStroke = new Stroke(Color.white);

                    strokes.add(currentStroke);

                    // Dodaj pierwszy punkt
                    currentStroke.points.add(
                            new Point(e.getX(), e.getY())
                    );

                    repaint();
                }
                else{
                    // Tworzymy nową kreskę z aktualnym kolorem
                    currentStroke = new Stroke(currentColor);

                    strokes.add(currentStroke);

                    // Dodaj pierwszy punkt
                    currentStroke.points.add(
                            new Point(e.getX(), e.getY())
                    );

                    repaint();
                }


            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isDrawing = false;
            }
        });


        // Przeciąganie myszy
        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {

                if (isDrawing) {

                    currentStroke.points.add(
                            new Point(e.getX(), e.getY())
                    );

                    repaint();
                }
            }
        });
    }


    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // Przechodzimy przez wszystkie kreski
        for (Stroke stroke : strokes) {

            // Ustawiamy kolor konkretnej kreski
            g.setColor(stroke.color);

            // Przechodzimy przez punkty tej kreski
            for (int i = 1; i < stroke.points.size(); i++) {

                Point previous = stroke.points.get(i - 1);
                Point current = stroke.points.get(i);

                g.drawLine(
                        previous.x,
                        previous.y,
                        current.x,
                        current.y
                );
            }
        }
    }


    // Getter koloru
    public Color getCurrentColor() {
        return currentColor;
    }

    // Setter koloru
    public void setCurrentColor(Color color) {
        currentColor = color;
        isRubber = false;
    }

    public void clear(){
        strokes.clear();
        repaint();
    }

    public void rubber(){
        isRubber = !isRubber;
    }
}


// Klasa reprezentująca jedną kreskę
class Stroke {

    ArrayList<Point> points = new ArrayList<>();

    Color color;

    Stroke(Color color) {
        this.color = color;
    }
}
