#include <iostream>
#include <Windows.h>

using namespace std;

class saat
{
private:
	int harcanan_saat = 0;
	int harcanan_dakika = 0;
	int harcanan_saniye = 0;
	int saniye;
	int dakika;
	int saat1;
public:
	void set()
	{
		cout << "Saati giriniz..:"; cin >> saat1;
		cout << "Dakikayi giriniz..:";  cin >> dakika;
		cout << "Saniyeyi giriniz..:"; cin >> saniye;
	}
	void print()
	{
		cout << "Saat..: " << saat1 << " / " << dakika << " / " << saniye;
	}
	void saatAyar()
	{
		int k = 0;
		while (k < 1)
		{
			system("cls");
			cout << "Ekran basinda harcanan sure..: " <<harcanan_saat<<" saat "<< harcanan_dakika <<" dakika "<<harcanan_saniye<< " saniye";
			harcanan_saniye++;
			saniye++;
			if (harcanan_saniye == 60)
			{
				harcanan_saniye = 0;
				harcanan_dakika++;
			}
			if (harcanan_dakika == 60)
			{
				harcanan_dakika = 0;
				harcanan_saat++;
			}
			Sleep(1000);
		}
	}
}
;

int main()
{
	saat s1;
	//s1.set();
	s1.saatAyar();
}