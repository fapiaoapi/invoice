

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


public class TaxExample {

    public static void main(String[] args) throws UnsupportedEncodingException {

        // 设置标准输出流编码为 UTF-8
        System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));

        /**
         * 含税金额计算示例
         *
         *   不含税单价 = 含税单价/(1+ 税率)  noTaxDj = dj / (1 + sl)
         *   不含税金额 = 不含税单价*数量  noTaxJe = noTaxDj * spsl
         *   含税金额 = 含税单价*数量  je = dj * spsl
         *   税额 = 税额 = 1 / (1 + 税率) * 税率 * 含税金额  se = 1  / (1 + sl) * sl * je
         *    hjse= se1 + se2 + ... + seN
         *    jshj= je1 + je2 + ... + jeN
         *   价税合计 =合计金额+合计税额 jshj = hjje + hjse
         *
         */

        /**
         * 含税计算示例1  无价格  无数量
         * @link https://fa-piao.com/fapiao.html?action=data1&source=github
         *
         */
        example1();
        System.out.println("---------------------------------------------");

        /**
         * 含税计算示例2  有价格 有数量
         * @link https://fa-piao.com/fapiao.html?action=data3&source=github
         *
         */
        example2();
        System.out.println("---------------------------------------------");

        /**
         * 含税计算示例3  有价格自动算数量  购买猪肉1000元,16.8元/斤
         * @link https://fa-piao.com/fapiao.html?action=data5&source=github
         *
         */
        example3();
        System.out.println("---------------------------------------------");

        /**
         * 含税计算示例4  有数量自动算价格  购买接口服务1000元7次
         * @link https://fa-piao.com/fapiao.html?action=data7&source=github
         *
         */
        example4();
        System.out.println("---------------------------------------------");

        /**
         * 不含税计算示例
         *  金额 = 单价 * 数量  je = dj * spsl
         *  税额 = 金额 * 税率  se = je * sl
         *   hjse= se1 + se2 + ... + seN
         *   hjje= je1 + je2 + ... + jeN
         *  价税合计 =合计金额+合计税额 jshj = hjje + hjse
         *
         */


        /**
         *
         * 不含税计算示例1 无价格 无数量
         * @link https://fa-piao.com/fapiao.html?action=data2&source=github
         */
        noTaxExample1();
        System.out.println("---------------------------------------------");

        /**
         *
         * 不含税计算示例2  有价格 有数量
         *   一阶水费 1吨，单价2元/吨，税率0.03
         *   二阶水费 1吨，单价3元/吨，税率0.01
         * @link https://fa-piao.com/fapiao.html?action=data4&source=github
         */

        noTaxExample2();
        System.out.println("---------------------------------------------");

        /**
         * 不含税计算示例3  有价格自动算数量  购买猪肉1000元,16.8元/斤
         * @link https://fa-piao.com/fapiao.html?action=data6&source=github
         *
         */
        noTaxExample3();
        System.out.println("---------------------------------------------");

        /**
         * 不含税计算示例4  有数量自动算价格  购买接口服务1000元7次
         *
         * @link https://fa-piao.com/fapiao.html?action=data8&source=github
         *
         */
        noTaxExample4();
        System.out.println("---------------------------------------------");

        /**
         * 免税计算示例
         *  金额 = 单价 * 数量  je = dj * spsl
         *  税额 = 0
         *  hjse = se1 + se2 + ... + seN
         *  jshj = je1 + je2 + ... + jeN
         *  价税合计 =合计金额+合计税额 jshj = hjje + hjse
         * @link https://fa-piao.com/fapiao.html?action=data9&source=github
         */
        taxFreeExample();
    }



    /**
     * 含税计算示例1 - 无价格无数量
     */
    public static void example1() {
        int hsbz = 1; // 含税标志，0不含税，1含税
        boolean isIncludeTax = hsbz == 1;
        BigDecimal amount = new BigDecimal("200");
        BigDecimal sl = new BigDecimal("0.01");
        BigDecimal se = calculateTax(amount, sl, isIncludeTax,2);

        InvoiceData data = new InvoiceData();
        data.fyxm = new ArrayList<>();

        InvoiceItem item = new InvoiceItem();
        item.fphxz = 0;
        item.hsbz = hsbz;
        item.spmc = "*软件维护服务*接口服务费";
        item.spbm = "3040201030000000000";
        item.je = amount;
        item.sl = sl;
        item.se = se;
        data.fyxm.add(item);

        // 计算合计值
        calculateTotals(data,isIncludeTax);

        System.out.println("含税计算示例1  无价格  无数量: ");
        System.out.println(data.toJson());
    }

    /**
     * 含税计算示例2 - 有价格有数量
     */
    public static void example2() {
        int hsbz = 1; // 含税标志，0不含税，1含税
        boolean isIncludeTax = hsbz == 1;
        // 第一个商品
        BigDecimal spsl1 = new BigDecimal("1");
        BigDecimal dj1 = new BigDecimal("2");
        BigDecimal sl1 = new BigDecimal("0.03");
        BigDecimal je1 = dj1.multiply(spsl1).setScale(2, RoundingMode.HALF_UP);
        BigDecimal se1 = calculateTax(je1, sl1, isIncludeTax,2);

        // 第二个商品
        BigDecimal spsl2 = new BigDecimal("1");
        BigDecimal dj2 = new BigDecimal("3");
        BigDecimal sl2 = new BigDecimal("0.01");
        BigDecimal je2 = dj2.multiply(spsl2).setScale(2, RoundingMode.HALF_UP);
        BigDecimal se2 = calculateTax(je2, sl2, isIncludeTax,2);

        InvoiceData data = new InvoiceData();
        data.fyxm = new ArrayList<>();

        // 添加第一个商品
        InvoiceItem item1 = new InvoiceItem();
        item1.fphxz = 0;
        item1.hsbz = hsbz;
        item1.spmc = "*水冰雪*一阶水费";
        item1.spbm = "1100301030000000000";
        item1.ggxh = "";
        item1.dw = "吨";
        item1.dj = dj1;
        item1.spsl = spsl1;
        item1.je = je1;
        item1.sl = sl1;
        item1.se = se1;
        data.fyxm.add(item1);

        // 添加第二个商品
        InvoiceItem item2 = new InvoiceItem();
        item2.fphxz = 0;
        item2.hsbz = hsbz;
        item2.spmc = "*水冰雪*二阶水费";
        item2.spbm = "1100301030000000000";
        item2.ggxh = "";
        item2.dw = "吨";
        item2.dj = dj2;
        item2.spsl = spsl2;
        item2.je = je2;
        item2.sl = sl2;
        item2.se = se2;
        data.fyxm.add(item2);

        // 计算合计值
        calculateTotals(data,isIncludeTax);

        System.out.println("含税计算示例2  有价格 有数量: ");
        System.out.println(data.toJson());
    }

    /**
     * 含税计算示例3 - 有价格自动算数量
     */
    public static void example3() {
        int hsbz = 1; // 含税标志，0不含税，1含税
        boolean isIncludeTax = hsbz == 1;
        BigDecimal amount = new BigDecimal("1000");
        BigDecimal dj = new BigDecimal("16.8");
        BigDecimal sl = new BigDecimal("0.01");
        BigDecimal se = calculateTax(amount, sl, isIncludeTax,2);

        InvoiceData data = new InvoiceData();
        data.fyxm = new ArrayList<>();

        InvoiceItem item = new InvoiceItem();
        item.fphxz = 0;
        item.hsbz = hsbz;
        item.spmc = "*肉类*猪肉";
        item.spbm = "1030107010100000000";
        item.ggxh = "";
        item.dw = "斤";
        item.dj = dj;
        item.spsl = amount.divide(dj, 13, RoundingMode.HALF_UP);
        item.je = amount;
        item.sl = sl;
        item.se = se;
        data.fyxm.add(item);

        // 计算合计值
        calculateTotals(data,isIncludeTax);

        System.out.println("含税计算示例3  有价格自动算数量 购买猪肉1000元,16.8元/斤: ");
        System.out.println(data.toJson());
    }

    /**
     * 含税计算示例4 - 有数量自动算价格
     */
    public static void example4() {
        int hsbz = 1; // 含税标志，0不含税，1含税
        boolean isIncludeTax = hsbz == 1;
        BigDecimal amount = new BigDecimal("1000");
        BigDecimal spsl = new BigDecimal("7");
        BigDecimal sl = new BigDecimal("0.01");
        BigDecimal se = calculateTax(amount, sl, isIncludeTax,2);

        InvoiceData data = new InvoiceData();
        data.fyxm = new ArrayList<>();

        InvoiceItem item = new InvoiceItem();
        item.fphxz = 0;
        item.hsbz = hsbz;
        item.spmc = "*软件维护服务*接口服务费";
        item.spbm = "3040201030000000000";
        item.ggxh = "";
        item.dw = "次";
        item.dj = amount.divide(spsl, 13, RoundingMode.HALF_UP);
        item.spsl = spsl;
        item.je = amount;
        item.sl = sl;
        item.se = se;
        data.fyxm.add(item);

        // 计算合计值
        calculateTotals(data,isIncludeTax);

        System.out.println("含税计算示例4  有数量自动算价格 购买接口服务1000元7次: ");
        System.out.println(data.toJson());
    }

    /**
     * 计算发票数据的合计值
     */
    private static void calculateTotals(InvoiceData data,boolean isIncludeTax) {
        BigDecimal jshj = BigDecimal.ZERO;
        BigDecimal hjse = BigDecimal.ZERO;

        if (isIncludeTax){
            for (InvoiceItem item : data.fyxm) {
                jshj = jshj.add(item.je);
                hjse = hjse.add(item.se);
            }

            data.jshj = jshj.setScale(2, RoundingMode.HALF_UP);
            data.hjse = hjse.setScale(2, RoundingMode.HALF_UP);
            data.hjje = jshj.subtract(hjse).setScale(2, RoundingMode.HALF_UP);
        }else{
            BigDecimal hjje = BigDecimal.ZERO;
            for (InvoiceItem item : data.fyxm) {
                hjje = hjje.add(item.je);
                hjse = hjse.add(item.se);
            }
            data.jshj = hjje.add(hjse).setScale(2, RoundingMode.HALF_UP);
            data.hjje = hjje.setScale(2, RoundingMode.HALF_UP);
            data.hjse = hjse.setScale(2, RoundingMode.HALF_UP);
        }

    }

    /**
     * 发票数据类
     */
    static class InvoiceData {
        public BigDecimal hjje = BigDecimal.ZERO;
        public BigDecimal hjse = BigDecimal.ZERO;
        public BigDecimal jshj = BigDecimal.ZERO;
        public List<InvoiceItem> fyxm;

        public String toJson() {
            StringBuilder sb = new StringBuilder();
            sb.append("{\n");
            sb.append("  \"hjje\": ").append(hjje).append(",\n");
            sb.append("  \"hjse\": ").append(hjse).append(",\n");
            sb.append("  \"jshj\": ").append(jshj).append(",\n");
            sb.append("  \"fyxm\": [\n");

            for (int i = 0; i < fyxm.size(); i++) {
                sb.append(fyxm.get(i).toJson());
                if (i < fyxm.size() - 1) {
                    sb.append(",\n");
                } else {
                    sb.append("\n");
                }
            }

            sb.append("  ]\n");
            sb.append("}");
            return sb.toString();
        }
    }
    /**
     * 不含税计算示例1 - 无价格无数量
     */
    public static void noTaxExample1() {
        int hsbz = 0; // 含税标志，0不含税，1含税
        boolean isIncludeTax = hsbz == 1;
        BigDecimal amount = new BigDecimal("200");
        BigDecimal sl = new BigDecimal("0.01");
        BigDecimal se = calculateTax(amount, sl, isIncludeTax,2);

        InvoiceData data = new InvoiceData();
        data.fyxm = new ArrayList<>();

        InvoiceItem item = new InvoiceItem();
        item.fphxz = 0;
        item.hsbz = hsbz;
        item.spmc = "*软件维护服务*接口服务费";
        item.spbm = "3040201030000000000";
        item.je = amount;
        item.sl = sl;
        item.se = se;
        data.fyxm.add(item);

        // 计算合计值
        calculateTotals(data,isIncludeTax);

        System.out.println("不含税计算示例1  无价格  无数量: ");
        System.out.println(data.toJson());
    }

    /**
     * 不含税计算示例2 - 有价格有数量
     */
    public static void noTaxExample2() {
        int hsbz = 0; // 含税标志，0不含税，1含税
        boolean isIncludeTax = hsbz == 1;
        // 第一个商品
        BigDecimal spsl1 = new BigDecimal("1");
        BigDecimal dj1 = new BigDecimal("2");
        BigDecimal sl1 = new BigDecimal("0.03");
        BigDecimal je1 = dj1.multiply(spsl1).setScale(2, RoundingMode.HALF_UP);
        BigDecimal se1 = calculateTax(je1, sl1, isIncludeTax,2);

        // 第二个商品
        BigDecimal spsl2 = new BigDecimal("1");
        BigDecimal dj2 = new BigDecimal("3");
        BigDecimal sl2 = new BigDecimal("0.01");
        BigDecimal je2 = dj2.multiply(spsl2).setScale(2, RoundingMode.HALF_UP);
        BigDecimal se2 = calculateTax(je2, sl2, isIncludeTax,2);

        InvoiceData data = new InvoiceData();
        data.fyxm = new ArrayList<>();

        // 添加第一个商品
        InvoiceItem item1 = new InvoiceItem();
        item1.fphxz = 0;
        item1.hsbz = hsbz;
        item1.spmc = "*水冰雪*一阶水费";
        item1.spbm = "1100301030000000000";
        item1.ggxh = "";
        item1.dw = "吨";
        item1.dj = dj1;
        item1.spsl = spsl1;
        item1.je = je1;
        item1.sl = sl1;
        item1.se = se1;
        data.fyxm.add(item1);

        // 添加第二个商品
        InvoiceItem item2 = new InvoiceItem();
        item2.fphxz = 0;
        item2.hsbz = hsbz;
        item2.spmc = "*水冰雪*二阶水费";
        item2.spbm = "1100301030000000000";
        item2.ggxh = "";
        item2.dw = "吨";
        item2.dj = dj2;
        item2.spsl = spsl2;
        item2.je = je2;
        item2.sl = sl2;
        item2.se = se2;
        data.fyxm.add(item2);

        // 计算合计值
        calculateTotals(data,isIncludeTax);

        System.out.println("不含税计算示例2  有价格 有数量: ");
        System.out.println(data.toJson());
    }

    /**
     * 不含税计算示例3 - 有价格自动算数量
     */
    public static void noTaxExample3() {
        int hsbz = 0; // 含税标志，0不含税，1含税
        boolean isIncludeTax = hsbz == 1;
        BigDecimal amount = new BigDecimal("1000");
        BigDecimal dj = new BigDecimal("16.8");
        BigDecimal sl = new BigDecimal("0.01");
        BigDecimal se = calculateTax(amount, sl, isIncludeTax,2);

        InvoiceData data = new InvoiceData();
        data.fyxm = new ArrayList<>();

        InvoiceItem item = new InvoiceItem();
        item.fphxz = 0;
        item.hsbz = hsbz;
        item.spmc = "*肉类*猪肉";
        item.spbm = "1030107010100000000";
        item.ggxh = "";
        item.dw = "斤";
        item.dj = dj;
        item.spsl = amount.divide(dj, 13, RoundingMode.HALF_UP);
        item.je = amount;
        item.sl = sl;
        item.se = se;
        data.fyxm.add(item);

        // 计算合计值
        calculateTotals(data,isIncludeTax);

        System.out.println("不含税计算示例3  有价格自动算数量 购买猪肉1000元,16.8元/斤: ");
        System.out.println(data.toJson());
    }

    /**
     * 不含税计算示例4 - 有数量自动算价格
     */
    public static void noTaxExample4() {
        int hsbz = 0; // 含税标志，0不含税，1含税
        boolean isIncludeTax = hsbz == 1;
        BigDecimal amount = new BigDecimal("1000");
        BigDecimal spsl = new BigDecimal("7");
        BigDecimal sl = new BigDecimal("0.01");
        BigDecimal se = calculateTax(amount, sl, isIncludeTax,2);

        InvoiceData data = new InvoiceData();
        data.fyxm = new ArrayList<>();

        InvoiceItem item = new InvoiceItem();
        item.fphxz = 0;
        item.hsbz = hsbz;
        item.spmc = "*软件维护服务*接口服务费";
        item.spbm = "3040201030000000000";
        item.ggxh = "";
        item.dw = "次";
        item.dj = amount.divide(spsl, 13, RoundingMode.HALF_UP);
        item.spsl = spsl;
        item.je = amount;
        item.sl = sl;
        item.se = se;
        data.fyxm.add(item);

        // 计算合计值
        calculateTotals(data,isIncludeTax);

        System.out.println("不含税计算示例4  有数量自动算价格 购买接口服务1000元7次: ");
        System.out.println(data.toJson());
    }

    /**
     * 免税计算示例
     */
    public static void taxFreeExample() {
        int hsbz = 0; // 含税标志，0不含税，1含税
        boolean isIncludeTax = hsbz == 1;
        BigDecimal dj = new BigDecimal("32263.98");
        BigDecimal sl = BigDecimal.ZERO;
        BigDecimal se = BigDecimal.ZERO;

        InvoiceData data = new InvoiceData();
        data.fyxm = new ArrayList<>();

        InvoiceItem item = new InvoiceItem();
        item.fphxz = 0;
        item.hsbz = hsbz;
        item.spmc = "*经纪代理服务*国际货物运输代理服务";
        item.spbm = "3040802010200000000";
        item.ggxh = "";
        item.dw = "次";
        item.spsl = BigDecimal.ONE;
        item.dj = dj;
        item.je = dj;
        item.sl = sl;
        item.se = se;
        item.yhzcbs = 1;
        item.lslbs = 1;
        item.zzstsgl = "免税";
        data.fyxm.add(item);

        // 计算合计值
        calculateTotals(data,isIncludeTax);

        System.out.println("免税计算示例: ");
        System.out.println(data.toJson());
    }


    /**
     * 计算税额
     *
     * @param amount       金额
     * @param taxRate      税率
     * @param isIncludeTax 是否含税
     * @param newScale     小数位数
     * @return BigDecimal
     */
    public static BigDecimal calculateTax(BigDecimal amount, BigDecimal taxRate, boolean isIncludeTax,int newScale) {
        BigDecimal tax;
        if(taxRate == null || taxRate.compareTo(BigDecimal.ZERO) <= 0){
            tax = BigDecimal.ZERO;
            return tax;
        }
        if (isIncludeTax) {
//            //旧 含税计算：税额 = 金额 / (1 + 税率) * 税率
//            tax = amount.divide(BigDecimal.ONE.add(taxRate), 10, RoundingMode.HALF_UP)
//                    .multiply(taxRate)
//                    .setScale(newScale, RoundingMode.HALF_UP);
            //新 含税计算：税额 = 1 / (1 + 税率) * 税率 * 含税金额
            tax = BigDecimal.ONE.divide(BigDecimal.ONE.add(taxRate), 10, RoundingMode.HALF_UP)
                    .multiply(taxRate).multiply(amount)
                    .setScale(newScale, RoundingMode.HALF_UP);
        } else {
            // 不含税计算：税额 = 金额 * 税率
            tax = amount.multiply(taxRate)
                    .setScale(newScale, RoundingMode.HALF_UP);
        }

        return tax;
    }



    /**
     * 发票项目类
     */
    static class InvoiceItem {
        public int fphxz;
        public int hsbz;
        public String spmc;
        public String spbm;
        public String ggxh = "";
        public String dw = "";
        public BigDecimal dj = BigDecimal.ZERO;
        public BigDecimal spsl = BigDecimal.ZERO;
        public BigDecimal je;
        public BigDecimal sl;
        public BigDecimal se;
        public int yhzcbs = 0;      // 优惠政策标识
        public int lslbs = 0;        // 零税率标识
        public String zzstsgl = "";  // 增值税特殊管理

        public String toJson() {
            StringBuilder sb = new StringBuilder();
            sb.append("    {\n");
            sb.append("      \"fphxz\": ").append(fphxz).append(",\n");
            sb.append("      \"hsbz\": ").append(hsbz).append(",\n");
            sb.append("      \"spmc\": \"").append(spmc).append("\",\n");
            sb.append("      \"spbm\": \"").append(spbm).append("\",\n");
            sb.append("      \"ggxh\": \"").append(ggxh).append("\",\n");
            sb.append("      \"dw\": \"").append(dw).append("\",\n");
            sb.append("      \"dj\": ").append(dj).append(",\n");
            sb.append("      \"spsl\": ").append(spsl).append(",\n");
            sb.append("      \"je\": ").append(je).append(",\n");
            sb.append("      \"sl\": ").append(sl).append(",\n");
            sb.append("      \"se\": ").append(se).append(",\n");
            sb.append("      \"yhzcbs\": ").append(yhzcbs).append(",\n");
            sb.append("      \"lslbs\": ").append(lslbs).append(",\n");
            sb.append("      \"zzstsgl\": \"").append(zzstsgl).append("\"\n");
            sb.append("    }");
            return sb.toString();
        }
    }



}





