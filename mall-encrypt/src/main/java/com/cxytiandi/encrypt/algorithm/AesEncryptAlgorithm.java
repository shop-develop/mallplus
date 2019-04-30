package com.cxytiandi.encrypt.algorithm;

import com.cxytiandi.encrypt.util.AesEncryptUtils;

/**
 * Aes加密算法实现
 * 
 * @author zscat
 * 
 * @date 2019-01-12
 * 
 * @about 2019-04-30
 *
 */
public class AesEncryptAlgorithm implements EncryptAlgorithm {

	@Override
	public String encrypt(String content, String encryptKey) throws Exception {
		return AesEncryptUtils.aesEncrypt(content, encryptKey);
	}

	@Override
	public String decrypt(String encryptStr, String decryptKey) throws Exception {
		return AesEncryptUtils.aesDecrypt(encryptStr, decryptKey);
	}

	public static void main(String[] args) {
		AesEncryptAlgorithm a = new AesEncryptAlgorithm();
		try {
			System.out.println(a.decrypt("2t9Flqu9yxOQOieasr6qRUzLsRxMmFrwiXNRNrup1g/dfIJn5O8zGdMABAN+/fO59iROJeSfoF7YyUUyvg+ZC4MKHhgvPrFtr75H15Xa7avGRR17n4vsyEmuB5ZjvUNIfW4HJ2PyXitwGJjiPv19EXwS9lW+YieHiCGIFwVEZ52e9V3Zy8z1buz1LUMPs04EAZKHCI1W0GDVURrVQxwBM/G5IxW0MW8hiWQXQ8/Vs3Dyav2Me5Wnu8IPWsWIdrFGx74p4ttD/GkVZAiM71lWmnJSW0NDA+38JMonkLeHmCjnbmwH2Js0+lxgxpRWXyt3X/CGzJOfgPF94u2d3WooNsZFHXufi+zISa4HlmO9Q0h9bgcnY/JeK3AYmOI+/X0RfBL2Vb5iJ4eIIYgXBURnnZ71XdnLzPVu7PUtQw+zTgQBkocIjVbQYNVRGtVDHAEzjWPa3WrlgTThWIVfKbOolPMqFly6NU5VpStFyQBrcADtGzRLjKXNzXXAQiORgXTrpwLo4ZIxQx2cr6XfKeEe5FM+VotHr7FjUQSzbzIdJGJznIcgcZnbK2HDemhHFjA7+QTHU6iBLVCV1ccwZGEk+jPuWf8fXo8NJR1NQLs1QGuuoWtMBJLYcwYX8jGKn7TyOeMUSGj1JJgfmqWJDAKq9fHRwxTBjFEFJI7gl7olksgngrxoOBoIFCMbUkFiqTTHbSBs+sBviqXEW638A6buiqVnN67xGDY1GMXhXcMK0UBdYHnkapRReCkgeHPmV4jPsZ2rOo6DXe1EFjb4ce0AQOwGXWYUDwUxxsgEhD4vI6DSZPscUMGtQ0Wy3BV9wDUWCdZ7/1kfKKLUTwAs9I/1Sm0Kb+SZhUNQE1H6TAfplT7oEUQg3ddD+CjHpNqZ8mjX6KCA6LhtqGp8BW4TVkquUTDgNUivJYwmQnQlrBfka2s4mePRJ1qRJ623ZwpqudKtA/ZxR13KnlRK6LR70QF7Qadrj7r8rR8KBy1gS8cKKFUojN9CBfNWOImXayfKidmHSaZvEYu4c48MK1Q4322G/5A/AKCRy7Mqu4qlTRVsHzFmqunY84G3dYNKSKkxeKM2p0sCw9dkVooIw4B1qWmC2w0lQphjpiOib+7JHwmPMsWgCgmDrql1PPBmO6DbwFlJs/nHWctio90mU7ZjJmQwoIoerDN3i1PMhBA/pxndZLRdU9jfiO13JK+LO6wW8iHB2XRmyEFD+sh5EY8rOJlYGNP7npClVRnC9pI44e1xIaQNPaEXM01Pui04KLKdbNul2R8k5Lb9h3twkWnnwCpLiGXlZ1J39vf1zaYFXmypZJNggaTzj12O7Q68u3z1FJbIfE3VC7Z6XbM13AkLXb4ftPCIJ6zVqIXn8kAsPyYl7Eguj9h3SA+v+Uho/pchuQX06WtMGPu0FCJl8Rg/IgJSzRJV8Q4tJBfao3XKgxeZ5xCV9ssdPXLnPOkOtk8npnnlrhPaF5uFAmopL1WAdGIrBUeVM3dVLYRGJTThdTCmP5l5IelpajfNszUxY56ZQcKp",
                    "abcdef0123456789"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
