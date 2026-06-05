import dao.CalidadDAO;
import dao.CartaDAO;
import model.Carta;
import model.Calidad;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    // ─── Colores ──────────────────────────────────────────────────────────────
    private static final Color C_FONDO_IZQ   = new Color(28, 38, 58);
    private static final Color C_FONDO_DER   = new Color(18, 22, 38);
    private static final Color C_CABECERA    = new Color(12, 16, 30);
    private static final Color C_BOTON       = new Color(40, 55, 90);
    private static final Color C_BOTON_HOVER = new Color(55, 75, 120);
    private static final Color C_TEXTO       = new Color(220, 225, 255);
    private static final Color C_SUBTITULO   = new Color(140, 155, 200);
    private static final Color C_TITULO      = new Color(232, 200, 74);

    // DAOs
    private final CartaDAO   cartaDAO   = new CartaDAO();
    private final CalidadDAO calidadDAO = new CalidadDAO();

    // Componentes
    private JPanel     cuadricula;
    private JTextField txtBuscar;
    private JCheckBox  cbComun, cbEspecial, cbEpica, cbLegendaria, cbCampeon;
    private JButton[]  botonesElixir = new JButton[9];

    public VentanaPrincipal() {
        setTitle("Clash Royale Card Manager");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(C_FONDO_DER);

        add(crearCabecera(),      BorderLayout.NORTH);
        add(crearPanelIzquierdo(), BorderLayout.WEST);
        add(crearPanelDerecho(),   BorderLayout.CENTER);

        configurarAcciones();
    }

    // CABECERA
    private JPanel crearCabecera() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_CABECERA);
        p.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titulo = new JLabel("CLASH ROYALE - CARTAS ", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(C_TITULO);
        p.add(titulo, BorderLayout.CENTER);
        return p;
    }

    // PANEL IZQUIERDO
    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(C_FONDO_IZQ);
        panel.setPreferredSize(new Dimension(200, 0));
        panel.setBorder(new EmptyBorder(16, 4, 16, 4));
        panel.setAlignmentX(LEFT_ALIGNMENT);

        //AGREGAR
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        fila.add(labelSeccion("AGREGAR", 16));
        panel.add(fila);
        panel.add(Box.createVerticalStrut(10));

        JButton btnNuevaCarta   = crearBoton("NUEVA CARTA +");
        JButton btnNuevaCalidad = crearBoton("NUEVA CALIDAD +");
        btnNuevaCarta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnNuevaCarta.setAlignmentX(LEFT_ALIGNMENT);
        btnNuevaCalidad.setAlignmentX(LEFT_ALIGNMENT);
        btnNuevaCalidad.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnNuevaCarta.setAlignmentX(LEFT_ALIGNMENT);
        btnNuevaCalidad.setAlignmentX(LEFT_ALIGNMENT);
        btnNuevaCarta.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnNuevaCalidad.setFont(new Font("SansSerif", Font.BOLD, 14));

        JPanel filaBtnCarta = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filaBtnCarta.setOpaque(false);
        filaBtnCarta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        filaBtnCarta.add(btnNuevaCarta);
        panel.add(filaBtnCarta);
        panel.add(Box.createVerticalStrut(4));

        JPanel filaBtnCal = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filaBtnCal.setOpaque(false);
        filaBtnCal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        filaBtnCal.add(btnNuevaCalidad);
        panel.add(filaBtnCal);
        panel.add(Box.createVerticalStrut(20));

        //FILTROS
        JPanel filaFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filaFiltros.setOpaque(false);
        filaFiltros.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        filaFiltros.add(labelSeccion("FILTROS", 16));
        panel.add(filaFiltros);
        panel.add(Box.createVerticalStrut(4));
        JPanel filaElixir = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filaElixir.setOpaque(false);
        filaElixir.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        filaElixir.add(labelSeccion("Elixir", 16));
        panel.add(filaElixir);
        panel.add(Box.createVerticalStrut(10));

        ImageIcon gota = iconoElixir(16);
        JPanel gridElixir = new JPanel(new GridLayout(3, 3, 8, 8));
        gridElixir.setOpaque(false);
        gridElixir.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        for (int i = 0; i < 9; i++) {
            JButton btn = new JButton(String.valueOf(i + 1));
            btn.setFont(new Font("SansSerif", Font.BOLD, 13));
            btn.setBackground(new Color(50, 30, 80));
            btn.setForeground(new Color(210, 170, 255));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(new Color(90, 50, 130), 1));
            if (gota != null) btn.setIcon(gota);
            botonesElixir[i] = btn;
            gridElixir.add(btn);
        }
        panel.add(gridElixir);
        panel.add(Box.createVerticalStrut(20));

        //CALIDADES
        JPanel filaCal = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filaCal.setOpaque(false);
        filaCal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        filaCal.add(labelSeccion("CALIDADES", 16));
        panel.add(filaCal);
        panel.add(Box.createVerticalStrut(10));

        cbComun      = crearCheckbox("Común",      new Color(170, 170, 170), 14);
        cbEspecial   = crearCheckbox("Especial",   new Color(255, 165,   0), 14);
        cbEpica      = crearCheckbox("Épica",      new Color(150,  50, 220), 14);
        cbLegendaria = crearCheckbox("Legendaria", new Color(135, 206, 250), 14);
        cbCampeon    = crearCheckbox("Campeón",    new Color(255, 215,   0), 14);

        for (JCheckBox cb : new JCheckBox[]{cbComun, cbEspecial, cbEpica, cbLegendaria, cbCampeon}) {
            JPanel filaCb = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            filaCb.setOpaque(false);
            filaCb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
            filaCb.add(cb);
            panel.add(filaCb);
            panel.add(Box.createVerticalStrut(4));
        }

        panel.add(Box.createVerticalGlue());

        btnNuevaCarta.addActionListener(e -> accionNuevaCarta());
        btnNuevaCalidad.addActionListener(e -> accionNuevaCalidad());

        return panel;
    }

    // PANEL DERECHO
    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(C_FONDO_DER);

        // Cabecera derecha
        JPanel cabDer = new JPanel(new BorderLayout(12, 0));
        cabDer.setBackground(C_CABECERA);
        cabDer.setBorder(new EmptyBorder(8, 14, 8, 14));

        JLabel lblBib = new JLabel("BIBLIOTECA DE CARTAS");
        lblBib.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblBib.setForeground(C_TEXTO);

        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        busqueda.setOpaque(false);
        JLabel lblBuscar = new JLabel("BUSCAR CARTA:");
        lblBuscar.setForeground(C_SUBTITULO);
        lblBuscar.setFont(new Font("SansSerif", Font.BOLD, 11));
        txtBuscar = new JTextField(18);
        txtBuscar.setBackground(new Color(30, 40, 65));
        txtBuscar.setForeground(C_TEXTO);
        txtBuscar.setCaretColor(Color.WHITE);
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 80, 120)),
                new EmptyBorder(4, 6, 4, 6)));
        busqueda.add(lblBuscar);
        busqueda.add(txtBuscar);

        cabDer.add(lblBib,    BorderLayout.WEST);
        cabDer.add(busqueda,  BorderLayout.EAST);
        panel.add(cabDer, BorderLayout.NORTH);

        // Cuadrícula
        cuadricula = new JPanel(new GridLayout(0, 4, 10, 10));
        cuadricula.setBackground(C_FONDO_DER);
        cuadricula.setBorder(new EmptyBorder(12, 12, 12, 12));

        JScrollPane scroll = new JScrollPane(cuadricula);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(C_FONDO_DER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    //ACCIONES
    private void configurarAcciones() {
        // Búsqueda por nombre (Enter)
        txtBuscar.addActionListener(e -> {
            String q = txtBuscar.getText().trim().toLowerCase();
            List<Carta> todas = cartaDAO.findAll();
            cuadricula.removeAll();
            for (Carta c : todas)
                if (c.getNombre().toLowerCase().contains(q))
                    cuadricula.add(crearTarjeta(c));
            refrescar();
        });

        // Checkboxes calidad
        ActionListener filtroCal = e -> aplicarFiltroCalidad();
        cbComun.addActionListener(filtroCal);
        cbEspecial.addActionListener(filtroCal);
        cbEpica.addActionListener(filtroCal);
        cbLegendaria.addActionListener(filtroCal);
        cbCampeon.addActionListener(filtroCal);

        // Botones elixir
        for (int i = 0; i < 9; i++) {
            final int elixir = i + 1;
            botonesElixir[i].addActionListener(e -> filtrarPorElixir(elixir));
        }
    }

    private void accionNuevaCarta() {
        String nombre  = JOptionPane.showInputDialog(this, "Nombre de la carta:");
        if (nombre == null || nombre.isBlank()) return;
        String elixirS = JOptionPane.showInputDialog(this, "Costo de elixir (1-9):");
        String tipo    = JOptionPane.showInputDialog(this, "Tipo (Tropa / Hechizo / Edificio):");
        String idCalS  = JOptionPane.showInputDialog(this,
                "ID Calidad:\n1=Común  2=Especial  3=Épica  4=Legendaria  5=Campeón");
        try {
            int elixir = Integer.parseInt(elixirS.trim());
            int idCal  = Integer.parseInt(idCalS.trim());
            Calidad cal = calidadDAO.findById(idCal);
            if (cal == null) { JOptionPane.showMessageDialog(this, "Calidad no encontrada."); return; }
            boolean ok = cartaDAO.insert(new Carta(0, nombre.trim(), elixir, tipo.trim(), cal));
            JOptionPane.showMessageDialog(this, ok ? "✔ Carta agregada." : "✘ Error al agregar.");
            cargarTodasLasCartas();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa números válidos.");
        }
    }

    private void accionNuevaCalidad() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la calidad:");
        if (nombre == null || nombre.isBlank()) return;
        boolean ok = calidadDAO.insert(new Calidad(0, nombre.trim()));
        JOptionPane.showMessageDialog(this, ok ? "✔ Calidad agregada." : "✘ Error.");
    }

    private void filtrarPorElixir(int elixir) {
        List<Carta> todas = cartaDAO.findAll();
        cuadricula.removeAll();
        for (Carta c : todas)
            if (c.getCosteElixir() == elixir)
                cuadricula.add(crearTarjeta(c));
        refrescar();
    }

    private void aplicarFiltroCalidad() {
        boolean ninguna = !cbComun.isSelected() && !cbEspecial.isSelected()
                && !cbEpica.isSelected() && !cbLegendaria.isSelected()
                && !cbCampeon.isSelected();
        List<Carta> todas = cartaDAO.findAll();
        cuadricula.removeAll();
        for (Carta c : todas) {
            String cal = c.getCalidad().getNombre().toLowerCase()
                    .replace("é","e").replace("ó","o").replace("ú","u");
            if (ninguna
                    || (cbComun.isSelected()      && cal.contains("comun"))
                    || (cbEspecial.isSelected()   && cal.contains("especial"))
                    || (cbEpica.isSelected()      && cal.contains("epica"))
                    || (cbLegendaria.isSelected() && cal.contains("legendaria"))
                    || (cbCampeon.isSelected()    && cal.contains("campeon")))
                cuadricula.add(crearTarjeta(c));
        }
        refrescar();
    }

    public void cargarTodasLasCartas() {
        List<Carta> lista = cartaDAO.findAll();
        cuadricula.removeAll();
        for (Carta c : lista) cuadricula.add(crearTarjeta(c));
        refrescar();
    }

    private void refrescar() {
        cuadricula.revalidate();
        cuadricula.repaint();
    }

    // TARJETA
    private JPanel crearTarjeta(Carta carta) {
        Color color = colorCalidad(carta.getCalidad().getNombre());

        JPanel tarjeta = new JPanel(new BorderLayout(4, 4));
        tarjeta.setBackground(color.darker().darker());
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2, true),
                new EmptyBorder(6, 6, 6, 6)));
        tarjeta.setPreferredSize(new Dimension(140, 175));
        tarjeta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Elixir arriba
        ImageIcon gota = iconoElixir(13);
        JLabel lblElixir = gota != null
                ? new JLabel(" " + carta.getCosteElixir(), gota, SwingConstants.LEFT)
                : new JLabel("💧 " + carta.getCosteElixir());
        lblElixir.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblElixir.setForeground(new Color(210, 170, 255));
        tarjeta.add(lblElixir, BorderLayout.NORTH);

        // Centro coloreado
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(color);
        JLabel lblTipo = new JLabel(carta.getTipo(), SwingConstants.CENTER);
        lblTipo.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblTipo.setForeground(new Color(255, 255, 255, 160));
        centro.add(lblTipo, BorderLayout.CENTER);
        tarjeta.add(centro, BorderLayout.CENTER);

        // Info abajo
        JPanel info = new JPanel(new GridLayout(2, 1, 0, 2));
        info.setOpaque(false);
        JLabel lblNombre = new JLabel(carta.getNombre(), SwingConstants.CENTER);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblNombre.setForeground(Color.WHITE);
        JLabel lblCal = new JLabel(carta.getCalidad().getNombre(), SwingConstants.CENTER);
        lblCal.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblCal.setForeground(color.brighter());
        info.add(lblNombre);
        info.add(lblCal);
        tarjeta.add(info, BorderLayout.SOUTH);

        // Clic → detalle
        tarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JOptionPane.showMessageDialog(null,
                        "ID: "      + carta.getId()
                                + "\nNombre: " + carta.getNombre()
                                + "\nElixir: " + carta.getCosteElixir()
                                + "\nTipo: "   + carta.getTipo()
                                + "\nCalidad: "+ carta.getCalidad().getNombre(),
                        "Detalle", JOptionPane.INFORMATION_MESSAGE);
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(color.brighter(), 3, true),
                        new EmptyBorder(6, 6, 6, 6)));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(color, 2, true),
                        new EmptyBorder(6, 6, 6, 6)));
            }
        });

        return tarjeta;
    }

    //HELPERS
    private Color colorCalidad(String calidad) {
        String c = calidad.toLowerCase()
                .replace("é", "e")
                .replace("ó", "o")
                .replace("ú", "u");
        if (c.contains("especial"))   return new Color(255, 165,   0);
        if (c.contains("epica"))      return new Color(150,  50, 220);
        if (c.contains("legendaria")) return new Color(135, 206, 250);
        if (c.contains("campeon"))    return new Color(255, 215,   0);
        return new Color(150, 150, 150); // Común
    }

    private ImageIcon iconoElixir(int size) {
        try {
            java.io.File f = new java.io.File(
                    "C:\\Users\\User\\IdeaProjects\\Clash Royale\\Docs\\elixir.png");
            if (!f.exists()) return null;
            Image img = new ImageIcon(f.getAbsolutePath())
                    .getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) { return null; }
    }

    private JLabel labelSeccion(String texto, int size) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, size));
        lbl.setForeground(C_TITULO);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(new Color(45, 60, 100));
        btn.setForeground(C_TITULO);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_TITULO, 1, true),
                new EmptyBorder(8, 14, 8, 14)));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(70, 90, 140));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(45, 60, 100));
            }
        });
        return btn;
    }

    private JCheckBox crearCheckbox(String texto, Color color, int size) {
        JCheckBox cb = new JCheckBox(texto);
        cb.setFont(new Font("SansSerif", Font.BOLD, size));
        cb.setForeground(color);
        cb.setOpaque(false);
        cb.setAlignmentX(LEFT_ALIGNMENT);
        cb.setHorizontalAlignment(SwingConstants.LEFT);
        return cb;
    }

    // MAIN
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
            catch (Exception ignored) {}
            VentanaPrincipal v = new VentanaPrincipal();
            v.cargarTodasLasCartas();
            v.setVisible(true);
        });
    }
}