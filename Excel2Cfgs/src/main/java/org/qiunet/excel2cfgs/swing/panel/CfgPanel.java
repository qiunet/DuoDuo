package org.qiunet.excel2cfgs.swing.panel;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.qiunet.excel2cfgs.common.constants.UiConstant;
import org.qiunet.excel2cfgs.common.enums.RoleType;
import org.qiunet.excel2cfgs.common.utils.Excel2CfgsUtil;
import org.qiunet.excel2cfgs.common.utils.SvnUtil;
import org.qiunet.excel2cfgs.setting.SettingManager;
import org.qiunet.excel2cfgs.swing.SwingUtil;
import org.qiunet.excel2cfgs.swing.component.IconJPanel;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;
import org.qiunet.excel2cfgs.swing.listener.JTreeMouseListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.*;

/***
 * 配置转换的面板
 *
 * @Author qiunet
 * @Date 2021/2/9 21:56
 **/
public class CfgPanel extends IconJPanel {
    /**
     * 缓存
     */
    private static final Map<String, DefaultMutableTreeNode> treeNodeCache = new HashMap<>();

    private JPanel showPanel;
    private JLabel currExcelPath;
    private JLabel currProCfgPath;
    private JLabel currProCfgPathNameLabel;
    private JTree excelPathTree;
    private JTextArea console;
    private JButton refreshBtn;
    private JButton svnUpdate;
    private JButton svnCommit;
    private JButton svnClean;
    private DefaultMutableTreeNode root;

    public CfgPanel() {
        if (!Excel2CfgsUtil.isWindows()) {
            svnCommit.setVisible(false);
        }
        excelPathTree.setBorder(new EmptyBorder(0, 0, 0, 0));
        refreshBtn.addActionListener(e -> this.loadFileTree());
        TitledBorder titledBorder = new TitledBorder("控制台");
        titledBorder.setTitleColor(Color.WHITE);
        titledBorder.setTitleFont(UiConstant.DEFAULT_FONT);
        console.setBorder(titledBorder);

        svnClean.addActionListener(e -> {
            String path = SettingManager.getInstance().getFirstExcelPath();
            SvnUtil.svnEvent(SvnUtil.SvnCommand.CLEANUP, path);
        });
        svnCommit.addActionListener(e -> {
            String path = SettingManager.getInstance().getFirstExcelPath();
            SvnUtil.svnEvent(SvnUtil.SvnCommand.COMMIT, path);
        });
        svnUpdate.addActionListener(e -> {
            String path = SettingManager.getInstance().getFirstExcelPath();
            SvnUtil.svnEvent(SvnUtil.SvnCommand.UPDATE, path);
            this.loadFileTree();
        });

        excelPathTree.setScrollsOnExpand(true);
        excelPathTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1 && event.getClickCount() == 2) {
                    File file = ((FileNode) ((DefaultMutableTreeNode) excelPathTree.getSelectionModel().getSelectionPath().getLastPathComponent()).getUserObject()).getFile();
                    if (!file.isFile()) {
                        return;
                    }

                    SwingUtil.open(file);
                }
            }
        });

        excelPathTree.addMouseListener(new JTreeMouseListener(excelPathTree));
        currExcelPath.setToolTipText("双击打开目录");
        currProCfgPath.setToolTipText("双击打开目录");
        currExcelPath.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    SwingUtil.open(new File(currExcelPath.getText()));
                }
            }
        });
        currProCfgPath.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    SwingUtil.open(new File(currProCfgPath.getText()));
                }
            }
        });
    }

    @Override
    public void unActivate() {

    }

    @Override
    public void activate() {
        this.currProCfgPathNameLabel.setVisible(SettingManager.getInstance().getSetting().getRoleType() != RoleType.SCHEMER);
        this.currProCfgPath.setVisible(SettingManager.getInstance().getSetting().getRoleType() != RoleType.SCHEMER);
        this.currProCfgPath.setText(SettingManager.getInstance().getFirstCfgPath());
        this.currExcelPath.setText(SettingManager.getInstance().getFirstExcelPath());

        String workPath = SettingManager.getInstance().getFirstExcelPath();
        if (workPath == null || workPath.length() == 0 || !new File(workPath).exists()) return;

        treeNodeCache.clear();
        File workPathFile = new File(workPath);
        root = new DefaultMutableTreeNode(new FileNode(workPathFile));
        this.loadFileTree();
    }

    @Override
    public IconButtonType type() {
        return IconButtonType.cfg;
    }

    @Override
    public JPanel getShowPanel() {
        return showPanel;
    }

    @Override
    public void addToParent(ToolTabPanel toolTabPanel) {
        toolTabPanel.getPanelUp().add(this.getButton());
    }

    /***
     * 加载文件树
     */
    private void loadFileTree() {
        root.removeAllChildren();
        this.loadTree(root);
        this.excelPathTree.setModel(new DefaultTreeModel(root));
        this.excelPathTree.repaint();
    }


    /***
     * 递归加载树
     * @param dirRoot
     */
    private void loadTree(DefaultMutableTreeNode dirRoot) {
        File dirFile = ((FileNode) dirRoot.getUserObject()).getFile();
        File[] files = dirFile.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (!filePostfixCheck(file)) continue;
            DefaultMutableTreeNode node = treeNodeCache.computeIfAbsent(file.getAbsolutePath(),
                    key -> new DefaultMutableTreeNode(new FileNode(file), file.isDirectory())
            );

            dirRoot.add(node);

            if (file.isDirectory()) {
                this.loadTree(node);
            }
        }
    }

    private static final Set<String> postfixs = new HashSet<>(Arrays.asList("xlsx", "xls", "xd", "xml", "json"));

    /**
     * 校验文件的后缀名
     *
     * @param file 校验的文件
     * @return true 符合
     */
    public static final boolean filePostfixCheck(File file) {
        String fileName = file.getName();

        if (fileName.startsWith("~") || fileName.startsWith(".")) return false;

        if (file.isFile()) {
            if (file.getName().contains(".")) {
                String postfix = file.getName().substring(file.getName().indexOf(".") + 1);
                return postfixs.contains(postfix);
            }
        }
        return true;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        showPanel = new JPanel();
        showPanel.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setEnabled(false);
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, 20, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Excel文件夹");
        showPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currExcelPath = new JLabel();
        currExcelPath.setEnabled(false);
        Font currExcelPathFont = this.$$$getFont$$$(null, -1, 16, currExcelPath.getFont());
        if (currExcelPathFont != null) currExcelPath.setFont(currExcelPathFont);
        currExcelPath.setText("D://directory/..");
        showPanel.add(currExcelPath, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currProCfgPathNameLabel = new JLabel();
        currProCfgPathNameLabel.setEnabled(false);
        Font currProCfgPathNameLabelFont = this.$$$getFont$$$(null, Font.BOLD, 20, currProCfgPathNameLabel.getFont());
        if (currProCfgPathNameLabelFont != null) currProCfgPathNameLabel.setFont(currProCfgPathNameLabelFont);
        currProCfgPathNameLabel.setText("项目导出文件夹");
        showPanel.add(currProCfgPathNameLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currProCfgPath = new JLabel();
        currProCfgPath.setEnabled(false);
        Font currProCfgPathFont = this.$$$getFont$$$(null, -1, 16, currProCfgPath.getFont());
        if (currProCfgPathFont != null) currProCfgPath.setFont(currProCfgPathFont);
        currProCfgPath.setText("D://directory/..");
        showPanel.add(currProCfgPath, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setBackground(new Color(-1));
        showPanel.add(panel1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Excel操作", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setBackground(new Color(-788494));
        panel1.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(250, 40), new Dimension(250, 40), new Dimension(250, 40), 0, false));
        refreshBtn = new JButton();
        refreshBtn.setBackground(new Color(-1773577));
        refreshBtn.setIcon(new ImageIcon(getClass().getResource("/icon/refresh_normal.png")));
        refreshBtn.setPressedIcon(new ImageIcon(getClass().getResource("/icon/refresh_enable.png")));
        refreshBtn.setRolloverIcon(new ImageIcon(getClass().getResource("/icon/refresh_rollover.png")));
        refreshBtn.setText("");
        refreshBtn.setToolTipText("刷新");
        panel2.add(refreshBtn, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(38, 38), new Dimension(38, 38), new Dimension(38, 38), 0, false));
        svnUpdate = new JButton();
        svnUpdate.setBackground(new Color(-1773577));
        svnUpdate.setIcon(new ImageIcon(getClass().getResource("/icon/svn_update_normal.png")));
        svnUpdate.setPressedIcon(new ImageIcon(getClass().getResource("/icon/svn_update_enable.png")));
        svnUpdate.setRolloverIcon(new ImageIcon(getClass().getResource("/icon/svn_update_rollover.png")));
        svnUpdate.setText("");
        svnUpdate.setToolTipText("Svn更新");
        panel2.add(svnUpdate, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(38, 38), new Dimension(38, 38), new Dimension(38, 38), 0, false));
        svnCommit = new JButton();
        svnCommit.setBackground(new Color(-1773577));
        svnCommit.setIcon(new ImageIcon(getClass().getResource("/icon/svn_commit_normal.png")));
        svnCommit.setPressedIcon(new ImageIcon(getClass().getResource("/icon/svn_commit_enable.png")));
        svnCommit.setRolloverIcon(new ImageIcon(getClass().getResource("/icon/svn_commit_rollover.png")));
        svnCommit.setText("");
        svnCommit.setToolTipText("Svn提交");
        panel2.add(svnCommit, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(38, 38), new Dimension(38, 38), new Dimension(38, 38), 0, false));
        svnClean = new JButton();
        svnClean.setBackground(new Color(-1773577));
        svnClean.setIcon(new ImageIcon(getClass().getResource("/icon/svn_clean_normal.png")));
        svnClean.setPressedIcon(new ImageIcon(getClass().getResource("/icon/svn_clean_enable.png")));
        svnClean.setRolloverIcon(new ImageIcon(getClass().getResource("/icon/svn_clean_rollover.png")));
        svnClean.setText("");
        svnClean.setToolTipText("Svn清理");
        panel2.add(svnClean, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(38, 38), new Dimension(38, 38), new Dimension(38, 38), 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(250, 430), new Dimension(250, 430), new Dimension(250, 430), 0, false));
        excelPathTree = new JTree();
        excelPathTree.setBackground(new Color(-1));
        Font excelPathTreeFont = this.$$$getFont$$$(null, -1, 18, excelPathTree.getFont());
        if (excelPathTreeFont != null) excelPathTree.setFont(excelPathTreeFont);
        excelPathTree.setMaximumSize(new Dimension(240, 410));
        scrollPane1.setViewportView(excelPathTree);
        final JSeparator separator1 = new JSeparator();
        showPanel.add(separator1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JSeparator separator2 = new JSeparator();
        showPanel.add(separator2, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        showPanel.add(scrollPane2, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(650, 500), new Dimension(650, 500), new Dimension(650, 500), 0, false));
        console = new JTextArea();
        console.setBackground(new Color(-13159113));
        console.setEditable(false);
        console.setEnabled(false);
        Font consoleFont = this.$$$getFont$$$(null, -1, 16, console.getFont());
        if (consoleFont != null) console.setFont(consoleFont);
        console.setMaximumSize(new Dimension(650, 500));
        console.setMinimumSize(new Dimension(650, 500));
        scrollPane2.setViewportView(console);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return showPanel;
    }

    public static class FileNode {
        private final File file;

        public FileNode(File file) {
            this.file = file;
        }

        public File getFile() {
            return file;
        }

        @Override
        public String toString() {
            return file.getName();
        }
    }

    public JTextArea getConsole() {
        return console;
    }
}
