package common;

/**
 * 
 * @author Sebastien Gislais
 *
 *Cette classe contient tous les attributs d'un Segment TCP
 */
public final class Segment {

    byte[] header;
    byte[] option;
    byte[] data;
    Long tmpi;
    Byte tmpb;

    public Segment() {
        int i;
        header = new byte[20];
        for (i = 0; i < 20; ++i) {
            header[i] = 0x00;
        }
        option = null;
        data = null;
    }

    /**
     * Permet de créer un segment a partir d'un dump binaire du segment;
     * @param b le dump binaire du segment
     */
    public Segment(byte[] b) {
        header = new byte[20];
        option = null;
        data = null;
        Long l;
        int i = 0;
        int olen = 0;
        int dlen = 0;
        Byte ln;
        Long li;

        for (i = 0; i < 20; ++i) {
            header[i] = b[i];
        }
        ln = b[12];
        li = ln.longValue();
        li = li & 0xF0;
        li = li >> 4;
        l = li.longValue();
        System.out.println("header len : " + l);
        if (l > 5) {
            olen = l.intValue() - 5;
            option = new byte[olen * 32];
            for (i = 0; i < (olen * 32); ++i) {
                option[i] = b[20 + i];
            }
        }
        dlen = (b.length - (((olen * 32)) + 20));
        data = new byte[dlen];
        for (i = 0; i < dlen; ++i) {
            data[i] = b[i + (olen * 32) + 20];
        }
    }

    public Segment(String s) {
        header = new byte[20];
        option = null;
        data = null;
        int i;
        int a;

        String tmps;
        String tt;
        long ll = 0;
        Long l = ll;
        Integer tmp = 0;
        boolean b = false;



        //s = "F0001000200000343000000040000000000001245";
        //System.out.println("___ SEGMENT ___ -- " + s);


        tmps = s.substring(0, 4);
        //System.out.println("Port source -- " + l.parseLong(tmps, 16)+ "   (" + tmps + ")");
        this.set_portsource(Long.parseLong(tmps, 16));
        tmps = s.substring(4, 8);
        //System.out.println("Pour dest -- " + l.parseLong(tmps, 16) + "   (" + tmps + ")");
        this.set_port_dest(Long.parseLong(tmps, 16));
        tmps = s.substring(8, 16);
        System.out.println("Seg numb -- " + Long.parseLong(tmps, 16) + "   (" + tmps + ")");
        this.set_sequence(Long.parseLong(tmps, 16));
        tmps = s.substring(16, 24);
        //System.out.println("Ack numb -- " + l.parseLong(tmps, 16) + "   (" + tmps + ")");
        this.set_ack(Long.parseLong(tmps, 16));
        tmps = s.substring(24, 25);
        //System.out.println("Offset -- " + l.parseLong(tmps, 16) + "   (" + tmps + ")");
        this.set_offset(Long.parseLong(tmps, 16));
        tmps = s.substring(25, 26);
        //System.out.println("Reserved -- " + l.parseLong(tmps, 16) + "   (" + tmps + ")");
        this.set_reserved(Long.parseLong(tmps, 16));
        tmps = s.substring(26, 27);
        //System.out.println("C -- " + l.parseLong(tmps, 16) + "   (" + tmps + ")");
        ll = Long.parseLong(tmps, 16);
        if ((ll & 0x8) != 0) {
            this.set_CWR(true);
        } else {
            this.set_CWR(false);
        }
        tmps = s.substring(26, 27);
        //System.out.println("E -- " + l.parseLong(tmps, 16) + "   (" + tmps + ")");
        this.set_ECN(b);
        tmps = s.substring(26, 27);
        //System.out.println("U -- " + l.parseLong(tmps, 16) + "   (" + tmps + ")");
        ll = Long.parseLong(tmps, 16);
        if ((ll & 0x2) != 0) {
            this.set_URG(true);
        } else {
            this.set_URG(false);
        }
        tmps = s.substring(26, 27);
        //System.out.println("A -- " + l.parseLong(tmps, 16) + "   (" + tmps + ")");
        ll = Long.parseLong(tmps, 16);
        if ((ll & 0x1) != 0) {
            this.set_ACK(true);
        } else {
            this.set_ACK(false);
        }
        tmps = s.substring(27, 28);
        //System.out.println("P -- " + l.parseLong(tmps, 16) + "   (" + tmps + ")");
        ll = Long.parseLong(tmps, 16);
        if ((ll & 0x8) != 0) {
            this.set_PSH(true);
        } else {
            this.set_PSH(false);
        }
        tmps = s.substring(27, 28);
        //System.out.println("R -- " + l.parseLong(tmps, 16) + "   (" + tmps + ")");
        ll = Long.parseLong(tmps, 16);
        if ((ll & 0x4) != 0) {
            this.set_RST(true);
        } else {
            this.set_RST(false);
        }
        tmps = s.substring(27, 28);
        //System.out.println("S -- " + l.parseLong(tmps, 16) + "   (" + tmps + ")");
        ll = Long.parseLong(tmps, 16);
        if ((ll & 0x2) != 0) {
            this.set_SYN(true);
        } else {
            this.set_SYN(false);
        }
        tmps = s.substring(27, 28);
        //System.out.println("F -- " + l.parseLong(tmps, 16) + "   (" + tmps + ")");
        ll = Long.parseLong(tmps, 16);
        if ((ll & 0x1) != 0) {
            this.set_FIN(true);
        } else {
            this.set_FIN(false);
        }
        tmps = s.substring(28, 32);
        //System.out.println("Window -- " + l.parseLong(tmps, 16) + "   (" + tmps + ")");
        this.set_window(Long.parseLong(tmps, 16));
        tmps = s.substring(28, 32);
        //System.out.println("Checksum -- " + l.parseLong(tmps, 16) + "   (" + tmps + ")");
        this.set_checksum(Long.parseLong(tmps, 16));
        tmps = s.substring(28, 32);
        //System.out.println("Urgent pointeur -- " + l.parseLong(tmps, 16) + "   (" + tmps + ")");
        this.set_pointeur_urgent(Long.parseLong(tmps, 16));

        tt = s.substring(32);
        data = new byte[(tt.length() / 2)];
        System.out.println("String : " + tt + "L=" + tt.length());
        for (i = 0, a = 0; i < (tt.length() - 2); i += 2, ++a) {
            if (tmps.equals("00")) {
            } else {
                tmps = tt.substring(i, i + 2);
                tmp = Integer.decode("0x" + tmps);
                data[a] = tmp.byteValue();
            }
        }
    }

    public void set_portsource(long port) {
        tmpi = (port & 0xFF00);
        tmpi = tmpi >> 8;
        System.out.println("prot set : " + tmpi);
        header[0] = tmpi.byteValue();
        tmpi = (port & 0xFF);

        System.out.println("prot set : " + tmpi);
        header[1] = tmpi.byteValue();

    }

    public void set_port_dest(long port) {
        tmpi = (port & 0xFF00);
        tmpi = tmpi >> 8;
        header[2] = tmpi.byteValue();
        tmpi = (port & 0xFF);
        header[3] = tmpi.byteValue();
    }

    public void set_sequence(long seq) {
        tmpi = (seq & 0xFF000000);
        header[4] = tmpi.byteValue();
        tmpi = (seq & 0xFF0000);
        header[5] = tmpi.byteValue();
        tmpi = (seq & 0xFF00);
        header[6] = tmpi.byteValue();
        tmpi = (seq & 0xFF);
        header[7] = tmpi.byteValue();
    }

    /*
    long ret = 0x00000000;
    tmpb = header[4];
    tmpi = tmpb.longValue();
    ret = tmpi << 24;
    tmpb = header[5];
    tmpi = tmpb.longValue();
    ret += tmpi << 16;
    tmpb = header[6];
    tmpi = tmpb.longValue();
    ret += tmpi << 8;
    tmpb = header[7];
    tmpi = tmpb.longValue();
    ret += tmpi;*/
    public void set_ack(long ack) {
        tmpi = (ack & 0xFF000000);
        header[8] = tmpi.byteValue();
        tmpi = (ack & 0xFF0000);
        header[9] = tmpi.byteValue();
        tmpi = (ack & 0xFF00);
        header[10] = tmpi.byteValue();
        tmpi = (ack & 0xFF);
        header[11] = tmpi.byteValue();
    }

    public void set_offset(long offset) {
        Long i;
        System.out.println("Offset set -- " + offset);
        tmpi = ((offset & 0x0f) << 4);
        System.out.println(tmpi + "--" + Long.toBinaryString(tmpi));
        tmpb = header[12];
        i = tmpb.longValue();
        i = i & 0xF0;
        i = tmpi | i;
        System.out.println("Set Offset : " + Long.toBinaryString(i));
        header[12] = i.byteValue();
    }

    public void set_reserved(long reserved) {
//		Long i;
//		
//		tmpi = (reserved & 0x0F);
//		tmpb = header[12];
//		i = tmpb.longValue();
//		i = i & 0xF;
//		i = tmpi | i;
//		header[12] = i.byteValue();
    }

    public void set_CWR(boolean b) {
        Long ih;

        tmpb = header[13];
        ih = tmpb.longValue();
        ih = ih & 0x7F;
        if (b == true) {
            ih = ih | 0x80;
        }
        header[13] = ih.byteValue();
    }

    public void set_ECN(boolean b) {
        Long ih;

        tmpb = header[13];
        ih = tmpb.longValue();
        ih = ih & 0xBF;
        if (b == true) {
            ih = ih | 0x40;
        }
        header[13] = ih.byteValue();
    }

    public void set_URG(boolean b) {
        Long ih;

        tmpb = header[13];
        ih = tmpb.longValue();
        ih = ih & 0xDF;
        if (b == true) {
            ih = ih | 0x20;
        }
        header[13] = ih.byteValue();
    }

    public void set_ACK(boolean b) {
        Long ih;

        tmpb = header[13];
        ih = tmpb.longValue();
        ih = ih & 0xEF;
        if (b == true) {
            ih = ih | 0x10;
        }
        header[13] = ih.byteValue();
    }

    public void set_PSH(boolean b) {
        Long ih;

        tmpb = header[13];
        ih = tmpb.longValue();
        ih = ih & 0xF7;
        if (b == true) {
            ih = ih | 0x08;
        }
        header[13] = ih.byteValue();
    }

    public void set_RST(boolean b) {
        Long ih;

        tmpb = header[13];
        ih = tmpb.longValue();
        ih = ih & 0xFB;
        if (b == true) {
            ih = ih | 0x04;
        }
        header[13] = ih.byteValue();
    }

    public void set_SYN(boolean b) {
        Long ih;

        tmpb = header[13];
        ih = tmpb.longValue();
        ih = ih & 0xFD;
        if (b == true) {
            ih = ih | 0x02;
        }
        header[13] = ih.byteValue();
    }

    public void set_FIN(boolean b) {
        Long ih;

        tmpb = header[13];
        ih = tmpb.longValue();
        ih = ih & 0xFE;
        if (b == true) {
            ih = ih | 0x01;
        }
        header[13] = ih.byteValue();
    }

    public void set_window(long win) {
        tmpi = win & 0xFF00;
        header[14] = tmpi.byteValue();
        tmpi = win & 0xFF;
        header[15] = tmpi.byteValue();
    }

    public void set_checksum(long chk) {
        tmpi = chk;
        tmpi = chk & 0xFF00;
        tmpi = tmpi >> 8;
        header[16] = tmpi.byteValue();
        tmpi = chk & 0xFF;
        header[17] = tmpi.byteValue();
    }

    public void set_pointeur_urgent(long pt) {
        tmpi = pt & 0xFF00;
        header[18] = tmpi.byteValue();
        tmpi = pt & 0xFF;
        header[19] = tmpi.byteValue();
    }

    public void set_option(byte[] opt) {
        int l = (opt.length / 4);
        int i = 0;
        int z = (opt.length % 4);

        if (z != 0) {
            z = 1;
        }
        option = new byte[l + (z * 4)];
        for (i = 0; i <= l; ++i) {
            for (z = 0; z < 4; ++z) {
                option[(i * 4) + z] = opt[(i * 4) + z];
            }
        }
        if (z != 0) {
            for (z = ((l * 4) + 1); z < opt.length; ++z) {
                option[z] = opt[z];
            }
            for (; z < ((l + 1) * 4); ++z) {
                option[z] = 0x00;
            }
        }
    }

    public void set_data(byte[] dat) {
        int i = 0;
        data = new byte[dat.length];
        int l = dat.length;

        for (i = 0; i < l; ++i) {
            data[i] = dat[i];
        }
    }

    public long get_port_source() {
        long ret = 0x00000000;
        tmpb = header[0];
        tmpi = tmpb.longValue();
        ret += (tmpi << 8);
        tmpb = header[1];
        tmpi = tmpb.longValue();
        ret += tmpi;
        return ret;
    }

    public long get_port_dest() {
        long ret = 0x00000000;
        tmpb = header[2];
        tmpi = tmpb.longValue();
        ret += tmpi << 8;
        tmpb = header[3];
        tmpi = tmpb.longValue();
        ret += tmpi;
        return ret;
    }

    public long get_seq_number() {
        long ret = 0x00000000;
        tmpb = header[4];
        tmpi = tmpb.longValue();
        ret = tmpi << 24;
        tmpb = header[5];
        tmpi = tmpb.longValue();
        ret += tmpi << 16;
        tmpb = header[6];
        tmpi = tmpb.longValue();
        ret += tmpi << 8;
        tmpb = header[7];
        tmpi = tmpb.longValue();
        ret += tmpi;
        return ret;
    }

    public long get_ack_number() {
        long ret = 0x00000000;
        tmpb = header[8];
        tmpi = tmpb.longValue();
        ret = tmpi << 12;
        tmpb = header[9];
        tmpi = tmpb.longValue();
        ret += tmpi << 8;
        tmpb = header[10];
        tmpi = tmpb.longValue();
        ret += tmpi << 4;
        tmpb = header[11];
        tmpi = tmpb.longValue();
        ret += tmpi;
        return ret;
    }

    public long get_offset() {
        tmpb = header[12];
        tmpi = tmpb.longValue();
        tmpi = tmpi & 0xf0;
        System.out.println("get  -- " + Long.toBinaryString(tmpi));

        tmpi = tmpi >> 4;

        System.out.println("get  -- " + Long.toBinaryString(tmpi));
        return tmpi;
    }

    public long get_reserved() {
        tmpb = header[12];
        tmpi = tmpb.longValue();
        tmpi = tmpi & 0xf;
        return tmpi;
    }

    public boolean get_CWR() {
        Byte a;
        Long b;

        a = header[13];
        b = (a.longValue() & 0x80) / 0x80;
        if (b == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean get_ECN() {
        Byte a;
        Long b;

        a = header[13];
        b = (a.longValue() & 0x40) / 0x40;
        if (b == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean get_URG() {
        Byte a;
        Long b;

        a = header[13];
        b = (a.longValue() & 0x20) / 0x20;
        if (b == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean get_ACK() {
        Byte a;
        Long b;

        a = header[13];
        b = (a.longValue() & 0x10) / 0x10;
        if (b == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean get_PSH() {
        Byte a;
        Long b;

        a = header[13];
        b = (a.longValue() & 0x08) / 0x08;
        if (b == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean get_RST() {
        Byte a;
        Long b;

        a = header[13];
        b = (a.longValue() & 0x04) / 0x04;
        if (b == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean get_SYN() {
        Byte a;
        Long b;

        a = header[13];
        b = (a.longValue() & 0x02) / 0x02;
        if (b == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean get_FIN() {
        Byte a;
        Long b;

        a = header[13];
        b = (a.longValue() & 0x01) / 0x01;
        if (b == 1) {
            return true;
        } else {
            return false;
        }
    }

    public long get_window() {
        return ((header[14] * 0x0100) + header[15]);
    }

    public long get_checksum() {
        Byte b1 = header[16];
        Byte b2 = header[17];
        Long l;

        l = ((b1.longValue() & 0xFF00)) + (b2 & 0xFF);
        return l;
    }

    public long get_pointeur_urgent() {
        return ((header[18] * 0x0100) + header[19]);
    }

    public byte[] get_option() {
        return option;
    }

    public byte[] get_data() {
        return data;
    }

    public long get_checksum(Segment seg) {
        long ret = 0x0000;
        long a1 = 0x0000;
        int l = 0;
        int i = 0;

        for (i = 0; i < 10; ++i) {
            if ((i * 2) == 16) {
                a1 = 0x0000;
            } else {
                a1 = header[i * 2] * 0x100 + header[(i * 2) + 1];
            }
            ret += ~a1;
        }
        if (option != null) {
            l = option.length / 2;
            for (i = 0; i < l; ++i) {
                a1 = option[i * 2] * 0x100 + option[(i * 2) + 1];
                ret += ~a1;
            }
            if ((option.length % 2) != 0) {
                a1 = option[option.length - 1] * 0x100 + 0x00;
                ret += ~a1;
            }
        }
        if (data != null) {
            l = data.length / 2;
            for (i = 0; i < l; ++i) {
                a1 = data[i * 2] * 0x100 + data[(i * 2) + 1];
                ret += ~a1;
            }
            if ((data.length % 2) != 0) {
                a1 = data[data.length - 1] * 0x100 + 0x00;
                ret += ~a1;
            }
        }
        ret = ~ret;
        return (ret & 0xFFFF);
    }

    /**
     * 
     * Cette méthode compare le checksum du segment avec 
     * le checksum calculer sur ce segment
     * 
     * @param seg le segment à verifier
     * @return
     */
    public boolean verif_checksum(Segment seg) {
        Long compute;
        Long store;

        compute = seg.get_checksum(seg);
        store = seg.get_checksum();
        if (store.longValue() == compute.longValue()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Convertie le segment TCP en binaire
     */
    public byte[] dumpbyte() {
        byte[] ret = null;
        int len = 0;
        int hlen = 0;
        int olen = 0;
        int dlen = 0;
        int i;

        hlen = header.length;
        if (option != null) {
            olen = option.length;
        }
        if (data != null) {
            dlen = data.length;
        }
        len = hlen + olen + dlen;
        ret = new byte[len];

        for (i = 0; i < hlen; ++i) {
            ret[i] = header[i];
        }

        if (option != null) {
            for (i = 0; i < olen; ++i) {
                ret[hlen + i] = option[i];
            }
        }

        if (data != null) {
            for (i = 0; i < data.length; ++i) {
                ret[olen + hlen + i] = data[i];
            }
        }

        return ret;
    }

    /**
     * 
     * Convertie le segment TCP en hexadecimal.
     * 
     * @return un dump du segment TCP
     */
    public String dump() {
        String ret = new String();
        Integer tmpi = 0;
        int i = 0;

        //System.out.println(this.get_seq_number());
        for (i = 0; i < this.header.length; ++i) {
            tmpi = (((Byte) (this.header[i])).intValue() & 0xF0) >> 4;
            ret += Integer.toHexString(tmpi);
            tmpi = (((Byte) (this.header[i])).intValue() & 0x0F);
            ret += Integer.toHexString(tmpi);
        }
        //System.out.println(ret);
        if (this.option != null) {
            for (i = 0; i < this.option.length; ++i) {
                tmpi = (((Byte) (this.option[i])).intValue() & 0xF0) >> 4;
                ret += Integer.toHexString(tmpi);
                tmpi = (((Byte) (this.option[i])).intValue() & 0x0F);
                ret += Integer.toHexString(tmpi);
            }
        }
        if (this.data != null) {
            for (i = 0; i < this.data.length; ++i) {
                tmpi = (((Byte) (this.data[i])).intValue() & 0xF0) >> 4;
                ret += Integer.toHexString(tmpi);
                tmpi = (((Byte) (this.data[i])).intValue() & 0x0F);
                ret += Integer.toHexString(tmpi);
            }
        }
        return ret;
    }

    public void display_text() {
        System.out.println("-------------------------------------  ");
        System.out.println("Port Source      : " + this.get_port_source());
        System.out.println("Port Destination : " + this.get_port_dest());
        System.out.println("Numéro de Seq    : " + this.get_seq_number());
        System.out.println("Numéro de Acq    : " + this.get_ack_number());
        System.out.println("Offset           : " + this.get_offset());
        System.out.println("Window           : " + this.get_window());
        System.out.println("Checksum         : " + this.get_checksum());
        System.out.println("Pointeur Urgent  : " + this.get_pointeur_urgent());
        System.out.println("URG              : " + this.get_URG());
        System.out.println("ACK              : " + this.get_ACK());
        System.out.println("SYN              : " + this.get_SYN());
        System.out.println("PSH              : " + this.get_PSH());
        System.out.println("RST              : " + this.get_RST());
        System.out.println("FIN              : " + this.get_FIN());
        System.out.println("Data             : " + this.get_data());



    }

    public static void main(String argv[]) {
    }
}
