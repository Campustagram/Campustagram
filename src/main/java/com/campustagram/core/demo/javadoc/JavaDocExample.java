package com.campustagram.core.demo.javadoc;

/**
 * Bu bir JavaDoc Örneğidir.
 *
 * @author  Ahmet SEN
 * @version 1.0.0
 * @since   2019-07-25
 */
public class JavaDocExample extends JavaDocDenek {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JavaDocExample jde = new JavaDocExample();
		jde.successfullyAttacked(66, 92, "aciklama");
	}

	/**
	 * <b>Bu doc yazıları uygulama bağımlı değil metod bağımlı olmalıdır. Bu metod
	 * bunu bunu yapar açıklaması yer almalıdır. Demo örnek aşağıdadır.</b>
	 * <p>
	 * Write the description to be implementation-independent, but specifying such
	 * dependencies where necessary. This helps engineers write code to be "write
	 * once, run anywhere."
	 * <p>
	 * As much as possible, write doc comments as an implementation-independent API
	 * specification.
	 * <p>
	 * <b>Eğer ki platform bağımlı ise bu açıklamaların en üstünde
	 * belirtilmelidir.</b>
	 * <p>
	 * <b>Uç değerler kesişim noktaları var ise bunlar belirtilmelidir. (örn: array
	 * size limit)</b>
	 * 
	 * 
	 * A <code>Bulp</code> is a bulp.
	 * <p>
	 * Lorem <b>Ipsum</b> is <i>simply</i> dummy text of the printing and
	 * typesetting industry. Lorem Ipsum has been the industry's standard dummy text
	 * ever since the 1500s, when an unknown printer took a galley of type and
	 * scrambled it to make a type specimen book.
	 * <p>
	 * Lorem Ipsum is simply dummy text of the printing and typesetting industry.
	 * Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,
	 * when an unknown printer took a galley of type and scrambled it to make a type
	 * specimen book.
	 * <p>
	 * <b>Açıklama için bir url, referans, ek açıklama gerekiyorsa bu şekilde
	 * belirtilmelidir.
	 * <a href="http://www.supermanisthegreatest.com">Superman!</a></b>
	 * <p>
	 * <b>burada parametrelerin sıralaması metod içerisinde verilen sıralama ile
	 * aynı olmalıdır. </b>
	 * 
	 * @author Captain America
	 * @param incomingDamage  : the amount of incoming damage
	 * @param incomingDefence : the amount of incoming defence
	 * @param aciklama        : buraya açıklama girilirken ilk cümlenin ilk harfi
	 *                        küçük olmalıdır. Eğer ki başka bir cümle yer alacaksa
	 *                        nokta ile ayrılmalıdır. 2. cümleden itibaren gelen her
	 *                        cümlenin ilk harfi büyük harf ile başlamalıdır.
	 * @return the amount of health hero has after attack
	 * @throws ArithmeticException incomingDamage 0 olursa hata oluşur
	 * @exception IllegalArgumentException in case of passing non-positive argument.
	 * @see <a href="http://www.link_to_jira/HERO-402">HERO-402</a>
	 * @see JavaDocDenek JavaDocDenek extend class
	 * @since 1.0
	 * @version 2.0
	 */
	public int successfullyAttacked(int incomingDamage, int incomingDefence, String aciklama)
			throws ArithmeticException {
		// do things
		JavaDocDenek.sayHello();
		if (0 == incomingDamage) {
			throw new ArithmeticException("Deneme Amaçlı");
		}
		return 0;
	}

}
