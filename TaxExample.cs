using System;
using System.Collections.Generic;
using System.Globalization;
using System.Text;

public class TaxExample
{
    public static void Main(string[] args)
    {
        Console.OutputEncoding = Encoding.UTF8;

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
        Console.WriteLine("---------------------------------------------");

        /**
         * 含税计算示例2  有价格 有数量
         * @link https://fa-piao.com/fapiao.html?action=data3&source=github
         *
         */
        example2();
        Console.WriteLine("---------------------------------------------");

        /**
         * 含税计算示例3  有价格自动算数量  购买猪肉1000元,16.8元/斤
         * @link https://fa-piao.com/fapiao.html?action=data5&source=github
         *
         */
        example3();
        Console.WriteLine("---------------------------------------------");

        /**
         * 含税计算示例4  有数量自动算价格  购买接口服务1000元7次
         * @link https://fa-piao.com/fapiao.html?action=data7&source=github
         *
         */
        example4();
        Console.WriteLine("---------------------------------------------");

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
        Console.WriteLine("---------------------------------------------");

        /**
         *
         * 不含税计算示例2  有价格 有数量
         *   一阶水费 1吨，单价2元/吨，税率0.03
         *   二阶水费 1吨，单价3元/吨，税率0.01
         * @link https://fa-piao.com/fapiao.html?action=data4&source=github
         *
         */

        noTaxExample2();
        Console.WriteLine("---------------------------------------------");

        /**
         * 不含税计算示例3  有价格自动算数量  购买猪肉1000元,16.8元/斤
         * @link https://fa-piao.com/fapiao.html?action=data6&source=github
         *
         */
        noTaxExample3();
        Console.WriteLine("---------------------------------------------");

        /**
         * 不含税计算示例4  有数量自动算价格  购买接口服务1000元7次
         *
         * @link https://fa-piao.com/fapiao.html?action=data8&source=github
         *
         */
        noTaxExample4();
        Console.WriteLine("---------------------------------------------");

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
    public static void example1()
    {
        int hsbz = 1;
        bool isIncludeTax = hsbz == 1;
        decimal amount = 200m;
        decimal sl = 0.01m;
        decimal se = calculateTax(amount, sl, isIncludeTax, 2);

        InvoiceData data = new InvoiceData();
        data.fyxm = new List<InvoiceItem>();

        InvoiceItem item = new InvoiceItem();
        item.fphxz = 0;
        item.hsbz = hsbz;
        item.spmc = "*软件维护服务*接口服务费";
        item.spbm = "3040201030000000000";
        item.je = amount;
        item.sl = sl;
        item.se = se;
        data.fyxm.Add(item);

        calculateTotals(data, isIncludeTax);

        Console.WriteLine("含税计算示例1  无价格  无数量: ");
        Console.WriteLine(data.toJson());
    }

    /**
     * 含税计算示例2 - 有价格有数量
     */
    public static void example2()
    {
        int hsbz = 1;
        bool isIncludeTax = hsbz == 1;
        decimal spsl1 = 1m;
        decimal dj1 = 2m;
        decimal sl1 = 0.03m;
        decimal je1 = Round(dj1 * spsl1, 2);
        decimal se1 = calculateTax(je1, sl1, isIncludeTax, 2);

        decimal spsl2 = 1m;
        decimal dj2 = 3m;
        decimal sl2 = 0.01m;
        decimal je2 = Round(dj2 * spsl2, 2);
        decimal se2 = calculateTax(je2, sl2, isIncludeTax, 2);

        InvoiceData data = new InvoiceData();
        data.fyxm = new List<InvoiceItem>();

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
        data.fyxm.Add(item1);

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
        data.fyxm.Add(item2);

        calculateTotals(data, isIncludeTax);

        Console.WriteLine("含税计算示例2  有价格 有数量: ");
        Console.WriteLine(data.toJson());
    }

    /**
     * 含税计算示例3 - 有价格自动算数量
     */
    public static void example3()
    {
        int hsbz = 1;
        bool isIncludeTax = hsbz == 1;
        decimal amount = 1000m;
        decimal dj = 16.8m;
        decimal sl = 0.01m;
        decimal se = calculateTax(amount, sl, isIncludeTax, 2);

        InvoiceData data = new InvoiceData();
        data.fyxm = new List<InvoiceItem>();

        InvoiceItem item = new InvoiceItem();
        item.fphxz = 0;
        item.hsbz = hsbz;
        item.spmc = "*肉类*猪肉";
        item.spbm = "1030107010100000000";
        item.ggxh = "";
        item.dw = "斤";
        item.dj = dj;
        item.spsl = Divide(amount, dj, 13);
        item.je = amount;
        item.sl = sl;
        item.se = se;
        data.fyxm.Add(item);

        calculateTotals(data, isIncludeTax);

        Console.WriteLine("含税计算示例3  有价格自动算数量 购买猪肉1000元,16.8元/斤: ");
        Console.WriteLine(data.toJson());
    }

    /**
     * 含税计算示例4 - 有数量自动算价格
     */
    public static void example4()
    {
        int hsbz = 1;
        bool isIncludeTax = hsbz == 1;
        decimal amount = 1000m;
        decimal spsl = 7m;
        decimal sl = 0.01m;
        decimal se = calculateTax(amount, sl, isIncludeTax, 2);

        InvoiceData data = new InvoiceData();
        data.fyxm = new List<InvoiceItem>();

        InvoiceItem item = new InvoiceItem();
        item.fphxz = 0;
        item.hsbz = hsbz;
        item.spmc = "*软件维护服务*接口服务费";
        item.spbm = "3040201030000000000";
        item.ggxh = "";
        item.dw = "次";
        item.dj = Divide(amount, spsl, 13);
        item.spsl = spsl;
        item.je = amount;
        item.sl = sl;
        item.se = se;
        data.fyxm.Add(item);

        calculateTotals(data, isIncludeTax);

        Console.WriteLine("含税计算示例4  有数量自动算价格 购买接口服务1000元7次: ");
        Console.WriteLine(data.toJson());
    }

    /**
     * 计算发票数据的合计值
     */
    private static void calculateTotals(InvoiceData data, bool isIncludeTax)
    {
        decimal jshj = 0m;
        decimal hjse = 0m;

        if (isIncludeTax)
        {
            foreach (InvoiceItem item in data.fyxm)
            {
                jshj += item.je;
                hjse += item.se;
            }

            data.jshj = Round(jshj, 2);
            data.hjse = Round(hjse, 2);
            data.hjje = Round(jshj - hjse, 2);
        }
        else
        {
            decimal hjje = 0m;
            foreach (InvoiceItem item in data.fyxm)
            {
                hjje += item.je;
                hjse += item.se;
            }
            data.jshj = Round(hjje + hjse, 2);
            data.hjje = Round(hjje, 2);
            data.hjse = Round(hjse, 2);
        }

    }

    /**
     * 发票数据类
     */
    class InvoiceData
    {
        public decimal hjje = 0m;
        public decimal hjse = 0m;
        public decimal jshj = 0m;
        public List<InvoiceItem> fyxm;

        public string toJson()
        {
            StringBuilder sb = new StringBuilder();
            sb.Append("{\n");
            sb.Append("  \"hjje\": ").Append(FormatDecimal(hjje)).Append(",\n");
            sb.Append("  \"hjse\": ").Append(FormatDecimal(hjse)).Append(",\n");
            sb.Append("  \"jshj\": ").Append(FormatDecimal(jshj)).Append(",\n");
            sb.Append("  \"fyxm\": [\n");

            for (int i = 0; i < fyxm.Count; i++)
            {
                sb.Append(fyxm[i].toJson());
                if (i < fyxm.Count - 1)
                {
                    sb.Append(",\n");
                }
                else
                {
                    sb.Append("\n");
                }
            }

            sb.Append("  ]\n");
            sb.Append("}");
            return sb.ToString();
        }
    }
    /**
     * 不含税计算示例1 - 无价格无数量
     */
    public static void noTaxExample1()
    {
        int hsbz = 0;
        bool isIncludeTax = hsbz == 1;
        decimal amount = 200m;
        decimal sl = 0.01m;
        decimal se = calculateTax(amount, sl, isIncludeTax, 2);

        InvoiceData data = new InvoiceData();
        data.fyxm = new List<InvoiceItem>();

        InvoiceItem item = new InvoiceItem();
        item.fphxz = 0;
        item.hsbz = hsbz;
        item.spmc = "*软件维护服务*接口服务费";
        item.spbm = "3040201030000000000";
        item.je = amount;
        item.sl = sl;
        item.se = se;
        data.fyxm.Add(item);

        calculateTotals(data, isIncludeTax);

        Console.WriteLine("不含税计算示例1  无价格  无数量: ");
        Console.WriteLine(data.toJson());
    }

    /**
     * 不含税计算示例2 - 有价格有数量
     */
    public static void noTaxExample2()
    {
        int hsbz = 0;
        bool isIncludeTax = hsbz == 1;
        decimal spsl1 = 1m;
        decimal dj1 = 2m;
        decimal sl1 = 0.03m;
        decimal je1 = Round(dj1 * spsl1, 2);
        decimal se1 = calculateTax(je1, sl1, isIncludeTax, 2);

        decimal spsl2 = 1m;
        decimal dj2 = 3m;
        decimal sl2 = 0.01m;
        decimal je2 = Round(dj2 * spsl2, 2);
        decimal se2 = calculateTax(je2, sl2, isIncludeTax, 2);

        InvoiceData data = new InvoiceData();
        data.fyxm = new List<InvoiceItem>();

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
        data.fyxm.Add(item1);

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
        data.fyxm.Add(item2);

        calculateTotals(data, isIncludeTax);

        Console.WriteLine("不含税计算示例2  有价格 有数量: ");
        Console.WriteLine(data.toJson());
    }

    /**
     * 不含税计算示例3 - 有价格自动算数量
     */
    public static void noTaxExample3()
    {
        int hsbz = 0;
        bool isIncludeTax = hsbz == 1;
        decimal amount = 1000m;
        decimal dj = 16.8m;
        decimal sl = 0.01m;
        decimal se = calculateTax(amount, sl, isIncludeTax, 2);

        InvoiceData data = new InvoiceData();
        data.fyxm = new List<InvoiceItem>();

        InvoiceItem item = new InvoiceItem();
        item.fphxz = 0;
        item.hsbz = hsbz;
        item.spmc = "*肉类*猪肉";
        item.spbm = "1030107010100000000";
        item.ggxh = "";
        item.dw = "斤";
        item.dj = dj;
        item.spsl = Divide(amount, dj, 13);
        item.je = amount;
        item.sl = sl;
        item.se = se;
        data.fyxm.Add(item);

        calculateTotals(data, isIncludeTax);

        Console.WriteLine("不含税计算示例3  有价格自动算数量 购买猪肉1000元,16.8元/斤: ");
        Console.WriteLine(data.toJson());
    }

    /**
     * 不含税计算示例4 - 有数量自动算价格
     */
    public static void noTaxExample4()
    {
        int hsbz = 0;
        bool isIncludeTax = hsbz == 1;
        decimal amount = 1000m;
        decimal spsl = 7m;
        decimal sl = 0.01m;
        decimal se = calculateTax(amount, sl, isIncludeTax, 2);

        InvoiceData data = new InvoiceData();
        data.fyxm = new List<InvoiceItem>();

        InvoiceItem item = new InvoiceItem();
        item.fphxz = 0;
        item.hsbz = hsbz;
        item.spmc = "*软件维护服务*接口服务费";
        item.spbm = "3040201030000000000";
        item.ggxh = "";
        item.dw = "次";
        item.dj = Divide(amount, spsl, 13);
        item.spsl = spsl;
        item.je = amount;
        item.sl = sl;
        item.se = se;
        data.fyxm.Add(item);

        calculateTotals(data, isIncludeTax);

        Console.WriteLine("不含税计算示例4  有数量自动算价格 购买接口服务1000元7次: ");
        Console.WriteLine(data.toJson());
    }

    /**
     * 免税计算示例
     */
    public static void taxFreeExample()
    {
        int hsbz = 0;
        bool isIncludeTax = hsbz == 1;
        decimal dj = 32263.98m;
        decimal sl = 0m;
        decimal se = 0m;

        InvoiceData data = new InvoiceData();
        data.fyxm = new List<InvoiceItem>();

        InvoiceItem item = new InvoiceItem();
        item.fphxz = 0;
        item.hsbz = hsbz;
        item.spmc = "*经纪代理服务*国际货物运输代理服务";
        item.spbm = "3040802010200000000";
        item.ggxh = "";
        item.dw = "次";
        item.spsl = 1m;
        item.dj = dj;
        item.je = dj;
        item.sl = sl;
        item.se = se;
        item.yhzcbs = 1;
        item.lslbs = 1;
        item.zzstsgl = "免税";
        data.fyxm.Add(item);

        calculateTotals(data, isIncludeTax);

        Console.WriteLine("免税计算示例: ");
        Console.WriteLine(data.toJson());
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
    public static decimal calculateTax(decimal amount, decimal taxRate, bool isIncludeTax, int newScale)
    {
        decimal tax;
        if (taxRate <= 0m)
        {
            tax = 0m;
            return tax;
        }
        if (isIncludeTax)
        {
            tax = Round(1m / (1m + taxRate), 10)
                * taxRate * amount;
            tax = Round(tax, newScale);
        }
        else
        {
            tax = Round(amount * taxRate, newScale);
        }

        return tax;
    }



    /**
     * 发票项目类
     */
    class InvoiceItem
    {
        public int fphxz;
        public int hsbz;
        public string spmc;
        public string spbm;
        public string ggxh = "";
        public string dw = "";
        public decimal dj = 0m;
        public decimal spsl = 0m;
        public decimal je;
        public decimal sl;
        public decimal se;
        public int yhzcbs = 0;
        public int lslbs = 0;
        public string zzstsgl = "";

        public string toJson()
        {
            StringBuilder sb = new StringBuilder();
            sb.Append("    {\n");
            sb.Append("      \"fphxz\": ").Append(fphxz).Append(",\n");
            sb.Append("      \"hsbz\": ").Append(hsbz).Append(",\n");
            sb.Append("      \"spmc\": \"").Append(spmc).Append("\",\n");
            sb.Append("      \"spbm\": \"").Append(spbm).Append("\",\n");
            sb.Append("      \"ggxh\": \"").Append(ggxh).Append("\",\n");
            sb.Append("      \"dw\": \"").Append(dw).Append("\",\n");
            sb.Append("      \"dj\": ").Append(FormatDecimal(dj)).Append(",\n");
            sb.Append("      \"spsl\": ").Append(FormatDecimal(spsl)).Append(",\n");
            sb.Append("      \"je\": ").Append(FormatDecimal(je)).Append(",\n");
            sb.Append("      \"sl\": ").Append(FormatDecimal(sl)).Append(",\n");
            sb.Append("      \"se\": ").Append(FormatDecimal(se)).Append(",\n");
            sb.Append("      \"yhzcbs\": ").Append(yhzcbs).Append(",\n");
            sb.Append("      \"lslbs\": ").Append(lslbs).Append(",\n");
            sb.Append("      \"zzstsgl\": \"").Append(zzstsgl).Append("\"\n");
            sb.Append("    }");
            return sb.ToString();
        }
    }

    private static decimal Round(decimal value, int scale)
    {
        return Math.Round(value, scale, MidpointRounding.AwayFromZero);
    }

    private static decimal Divide(decimal dividend, decimal divisor, int scale)
    {
        return Round(dividend / divisor, scale);
    }

    private static string FormatDecimal(decimal value)
    {
        return value.ToString(CultureInfo.InvariantCulture);
    }
}
