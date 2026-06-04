import dao.CalidadDAO;
import dao.CartaDAO;
import model.Carta;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionListener;

public class VentanaPrincipal extends JFrame {

    private JPanel panel1;
    private JButton NUEVACARTAButton;
    private JButton NUEVACALIDADButton;
    private JCheckBox comunCheckBox;
    private JCheckBox especialCheckBox;
    private JCheckBox epicaCheckBox;
    private JCheckBox legendariaCheckBox;
    private JCheckBox campeonCheckBox;
    private JTextField textField1;

//DAOs
    private final CartaDAO cartaDAO = new CartaDAO();
    private final  CalidadDAO calidadDAO = new CalidadDAO();

    //Panel de Cuadricula
    private JPanel cuadricula;

    public VentanaPrincipal()
    {
        setTitle("Clash Royale Card Manager");
        setSize(1050, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cuadricula = new JPanel(new GridLayout(0, 4, 10, 10));
        cuadricula.setBackground(new Color(25, 35, 55));

        JScrollPane scroll = new JScrollPane(cuadricula);
        scroll.setBorder(null);

        add(panel1, BorderLayout.WEST);
        add(scroll, BorderLayout.CENTER);

        configuararAcciones();

    }

    private void configuararAcciones() {

        //Boton Nueva Carta
        NUEVACARTAButton.addActionListener(e ->
        {
                String nombre = JOptionPane.showInputDialog(this, "Nombre de la carta:");
                if (nombre == null || nombre.isBlank()) return;
                String elixirS = JOptionPane.showInputDialog(this, "Costo de elixir (1-10):");
                String tipo = JOptionPane.showInputDialog(this,"Tipo (Tropa/Hachizo/Estructura):");
                String idCalS = JOptionPane.showInputDialog(this, "Calidad (Comun, Especial, Epica, Legendaria):");
                try {
                    int elixir = Integer.parseInt(elixirS);
                    int idCal = Integer.parseInt(idCalS);
                    model.Calidad cal = calidadDAO.findById(idCal);
                    if (cal == null) { JOptionPane.showMessageDialog(this, "Calidad no encontrada."); return; }
                    Carta c = new Carta(0, nombre, elixir, tipo, cal);
                    boolean ok = cartaDAO.insert(c);
                    JOptionPane.showMessageDialog(this, ok ? "✔ Carta agregada." : "✘ Error al agregar.");
                    cargarTodasLasCartas();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,"Ingresa numeros validos.");
                }
    });
        //Boton Nueva Calidad
    NUEVACALIDADButton.addActionListener(e -> {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la calidad:");
        if (nombre == null || nombre.isBlank()) return;
        boolean ok = calidadDAO.insert(new model.Calidad(0, nombre));
        JOptionPane.showMessageDialog(this, ok ? "✔ Calidad agregada." : "✘ Error.");
    });

    // ── Búsqueda por nombre ───────────────────────────────────────────────
        textField1.addActionListener(e -> {
        String busqueda = textField1.getText().trim().toLowerCase();
        List<Carta> todas = cartaDAO.findAll();
        cuadricula.removeAll();
        for (Carta c : todas) {
            if (c.getNombre().toLowerCase().contains(busqueda))
                cuadricula.add(crearTarjeta(c));
        }
        cuadricula.revalidate();
        cuadricula.repaint();
    });

    // ── Checkboxes de calidad ─────────────────────────────────────────────
    ActionListener filtroCalidad = e -> aplicarFiltroCalidad();
        comunCheckBox.addActionListener(filtroCalidad);
        especialCheckBox.addActionListener(filtroCalidad);
        epicaCheckBox.addActionListener(filtroCalidad);
        legendariaCheckBox.addActionListener(filtroCalidad);
        campeonCheckBox.addActionListener(filtroCalidad);
}

// ─── Carga todas las cartas en la cuadrícula ──────────────────────────────
public void cargarTodasLasCartas() {
    List<Carta> lista = cartaDAO.findAll();
    cuadricula.removeAll();
    for (Carta c : lista) cuadricula.add(crearTarjeta(c));
    cuadricula.revalidate();
    cuadricula.repaint();
}

// ─── Filtro por calidad con checkboxes ────────────────────────────────────
private void aplicarFiltroCalidad() {
    boolean ningunaMarcada = !comunCheckBox.isSelected() && !especialCheckBox.isSelected()
            && !epicaCheckBox.isSelected() && !legendariaCheckBox.isSelected()
            && !campeonCheckBox.isSelected();

    List<Carta> todas = cartaDAO.findAll();
    cuadricula.removeAll();
    for (Carta c : todas) {
        String cal = c.getCalidad().getNombre().toLowerCase();
        if (ningunaMarcada
                || (comunCheckBox.isSelected()     && cal.contains("comun"))
                || (especialCheckBox.isSelected()  && cal.contains("especial"))
                || (epicaCheckBox.isSelected()     && cal.contains("epica"))
                || (legendariaCheckBox.isSelected()&& cal.contains("legendaria"))
                || (campeonCheckBox.isSelected()   && cal.contains("campeon"))) {
            cuadricula.add(crearTarjeta(c));
        }
    }
    cuadricula.revalidate();
    cuadricula.repaint();
}

// ─── Crea una tarjeta visual para cada carta ──────────────────────────────
private JPanel crearTarjeta(Carta carta) {
    Color colorFondo = colorSegunCalidad(carta.getCalidad().getNombre());

    JPanel tarjeta = new JPanel(new BorderLayout(4, 4));
    tarjeta.setBackground(colorFondo.darker().darker());
    tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorFondo, 2, true),
            BorderFactory.createEmptyBorder(6, 6, 6, 6)));
    tarjeta.setPreferredSize(new Dimension(130, 160));

    // Elixir arriba
    JLabel lblElixir = new JLabel("💧 " + carta.getCosteElixir(), SwingConstants.LEFT);
    lblElixir.setFont(new Font("SansSerif", Font.BOLD, 12));
    lblElixir.setForeground(new Color(200, 160, 255));
    tarjeta.add(lblElixir, BorderLayout.NORTH);

    // Color central (placeholder de imagen)
    JPanel imgPlaceholder = new JPanel();
    imgPlaceholder.setBackground(colorFondo);
    imgPlaceholder.setPreferredSize(new Dimension(100, 80));
    imgPlaceholder.setBorder(BorderFactory.createLineBorder(colorFondo.brighter(), 1, true));
    JLabel lblTipo = new JLabel(carta.getTipo(), SwingConstants.CENTER);
    lblTipo.setForeground(Color.WHITE.darker());
    lblTipo.setFont(new Font("SansSerif", Font.ITALIC, 11));
    imgPlaceholder.setLayout(new BorderLayout());
    imgPlaceholder.add(lblTipo, BorderLayout.CENTER);
    tarjeta.add(imgPlaceholder, BorderLayout.CENTER);

    // Nombre e insignia calidad abajo
    JPanel info = new JPanel(new GridLayout(2, 1, 0, 2));
    info.setOpaque(false);

    JLabel lblNombre = new JLabel(carta.getNombre(), SwingConstants.CENTER);
    lblNombre.setFont(new Font("SansSerif", Font.BOLD, 11));
    lblNombre.setForeground(Color.WHITE);

    JLabel lblCal = new JLabel(carta.getCalidad().getNombre(), SwingConstants.CENTER);
    lblCal.setFont(new Font("SansSerif", Font.PLAIN, 10));
    lblCal.setForeground(colorFondo.brighter());

    info.add(lblNombre);
    info.add(lblCal);
    tarjeta.add(info, BorderLayout.SOUTH);

    // Clic en tarjeta → buscar por ID
    tarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            JOptionPane.showMessageDialog(null,
                    "ID: " + carta.getId() + "\nNombre: " + carta.getNombre()
                            + "\nElixir: " + carta.getCosteElixir()
                            + "\nTipo: " + carta.getTipo()
                            + "\nCalidad: " + carta.getCalidad().getNombre(),
                    "Detalle de Carta", JOptionPane.INFORMATION_MESSAGE);
        }
    });

    return tarjeta;
}

// ─── Color según calidad ──────────────────────────────────────────────────
private Color colorSegunCalidad(String calidad) {
    return switch (calidad.toLowerCase()) {
        case "especial"   -> new Color(70,  120, 200);
        case "epica"      -> new Color(130,  50, 180);
        case "legendaria" -> new Color(200, 150,  30);
        case "campeon"    -> new Color(200,  80,  30);
        default           -> new Color(60,  120,  60); // Comun
    };
}

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        VentanaPrincipal v = new VentanaPrincipal();
        v.cargarTodasLasCartas();
        v.setVisible(true);
    });
}
}


