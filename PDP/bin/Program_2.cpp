#include <iostream>
#include <fstream>
#include <string>
#include <sstream>

using namespace std;

class Dugum
{
public:
	Dugum* onceki;
	int veri;
	Dugum* sonraki;

	Dugum(Dugum* onck, Dugum* snrk, int vr)
	{
		veri = vr;
		onceki = onck;
		sonraki = snrk;
	}

	Dugum()
	{
		onceki = NULL;
		sonraki = NULL;
	}

	~Dugum()
	{

	}
};

class DosyaOku
{
private:
	int** sayilar;
	int satirSayisi;
	int* elemanSayisi;

public:

	DosyaOku()
	{
		satirSayisi = 0;

		ifstream DosyaOku("Sayilar.txt");

		//SatirSayisi atanmasi icin degiskenler
		string Sayilar = "";

		//ElemanSayisi atanmasi icin degiskenler
		int boslukSayisi = 0;
		int toplamEleman = 0;
		int eol = 0;

		//Sayilar atanmasi icin degiskenler
		string yeniSayilar = "";
		string YeniSayilar = "";
		string sayi = "";
		int i = 0;
		int karakter = 0;

		//Dosya okunmasi

		while (getline(DosyaOku, Sayilar))
		{
			yeniSayilar += Sayilar;
			YeniSayilar += Sayilar;
			yeniSayilar += '\n';
			YeniSayilar += '\n';
			satirSayisi++;
		}

		//Eleman sayisi atanmasi icin olsturulan dizisi
		elemanSayisi = new int[satirSayisi];

		//Eleman sayisi atanmasi
		for (;;)
		{
			if (eol == satirSayisi) break;
			if (yeniSayilar[i] == ' ') boslukSayisi++;
			if (yeniSayilar[i] == '\n')
			{
				toplamEleman = boslukSayisi + 1;
				elemanSayisi[eol] = toplamEleman;
				eol++;
				toplamEleman = 0;
				boslukSayisi = 0;
			}
			i++;
		}

		//sayilar dizisi icin heap bellek bolgesinde yer ayrilmasi
		sayilar = new int* [satirSayisi];

		for (int i = 0; i < satirSayisi; i++)
		{
			sayilar[i] = new int[elemanSayisi[i]];
		}

		//Sayilar atanmasi
		for (int i = 0; i < satirSayisi; i++)
		{
			for (int j = 0; j < elemanSayisi[i]; j++)
			{
				while (true)
				{

					if (YeniSayilar[karakter] != ' ')
					{
						if (YeniSayilar[karakter] != '\n')
						{
							sayi += YeniSayilar[karakter];
							karakter++;
						}
						else
						{
							sayilar[i][j] = stoi(sayi);
							sayi = "";
							karakter++;
							break;
						}
					}

					else
					{
						sayilar[i][j] = stoi(sayi);
						sayi = "";
						karakter++;
						break;
					}
				}
			}
		}
		DosyaOku.close();
	}

	int getSatirSayisi()
	{
		return satirSayisi;
	}

	int getElemanSayisi(int satir)
	{
		return elemanSayisi[satir - 1];
	}

	int** getSayilar()
	{
		return sayilar;
	}
	~DosyaOku()
	{
		delete[] sayilar;
		delete[] elemanSayisi;
	}
};

class Liste
{
private:
	Dugum* ortaDugum;
	int boyut;
	int basSayisi;
	int sonSayisi;

public:
	Liste()
	{
		ortaDugum = new Dugum();
		boyut = 0;
		basSayisi = 0;
		sonSayisi = 0;
	}

	void Ekle(int** sayilar, int elemanSayisi, int satirNumber)
	{
		int i = 0;
		for (;;)
		{
			if (i == satirNumber)
			{
				for (int j = 0; j < elemanSayisi; j++)
				{
					if (j == 0) basaEkle(sayilar[i][j]);
					else if (j <= (elemanSayisi - 1) / 2) basaEkle(sayilar[i][j]);
					else if (j >= (elemanSayisi - 1) / 2) sonaEkle(sayilar[i][j]);
				}
				break;
			}
			i++;
		}
	}

	void basaEkle(int veri)
	{
		if (boyut == 0)
		{
			ortaDugum->veri = veri;
			ortaDugum->onceki = ortaDugum;
			ortaDugum->sonraki = ortaDugum;
		}

		else
		{
			int sayac = 0;
			if (basSayisi == 0)
			{
				Dugum* nw = new Dugum(ortaDugum->onceki, ortaDugum, veri);
				ortaDugum->onceki = nw;
				nw->onceki->sonraki = nw;
			}
			else
			{
				Dugum* itr = ortaDugum;
				while (basSayisi != sayac++)
				{
					itr = itr->onceki;
				}
				Dugum* nw_2 = new Dugum(itr->onceki, itr, veri);
				itr->onceki = nw_2;
				nw_2->onceki->sonraki = nw_2;
			}
			basSayisi++;
		}
		boyut++;
	}

	Liste* listeYedekle(Liste* yedek, int elemanSay)
	{
		yedek->basaEkle(0);
		Dugum* itr = ortaDugum;
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < (elemanSay - 1) / 2; j++)
			{
				if (i == 0)
				{
					itr = itr->onceki;
					yedek->basaEkle(itr->veri);
				}

				else if (i == 1)
				{
					itr = itr->sonraki;
				}

				else if (i == 2)
				{
					itr = itr->sonraki;
					yedek->sonaEkle(itr->veri);
				}
			}
		}
		return yedek;
	}

	void sonaEkle(int veri)
	{
		int sayac = 0;
		if (sonSayisi == 0)
		{
			Dugum* nw = new Dugum(ortaDugum, ortaDugum->sonraki, veri);
			ortaDugum->sonraki = nw;
			nw->sonraki->onceki = nw;
		}
		else
		{
			Dugum* itr = ortaDugum;
			while (sonSayisi != sayac++)
			{
				itr = itr->sonraki;
			}
			Dugum* nw_2 = new Dugum(itr, itr->sonraki, veri);
			itr->sonraki = nw_2;
			nw_2->sonraki->onceki = nw_2;
		}
		sonSayisi++;
		boyut++;
	}

	void yedekGuncelle(Liste* yedek, int elemanNumber)
	{
		Dugum* itrYedek = yedek->getOrtaDugumAdres();
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < (elemanNumber - 1) / 2; j++)
			{
				if (i == 0)
				{
					itrYedek = itrYedek->sonraki;
					this->basaEkle(itrYedek->veri);
				}
				if (i == 1)
				{
					itrYedek = itrYedek->onceki;
				}
				if (i == 2)
				{
					itrYedek = itrYedek->onceki;
					this->sonaEkle(itrYedek->veri);
				}
			}
		}
	}

	void ortaDugumDisindaSil()
	{
		if (boyut == 1) {}
		else
		{
			Dugum* itr = ortaDugum->sonraki;
			while (itr != ortaDugum)
			{
				itr = itr->sonraki;
				delete itr->onceki;
			}
			delete ortaDugum->onceki;
			boyut = 1;
			basSayisi = 0;
			sonSayisi = 0;
			ortaDugum->sonraki = ortaDugum;
			ortaDugum->onceki = ortaDugum;
		}
	}

	void listeYazdir()
	{
		int sayac = 0;
		Dugum* itr = ortaDugum;
		while (basSayisi != sayac++)
		{
			itr = itr->onceki;
		}
		sayac = 0;
		for (itr; sayac < boyut; itr = itr->sonraki, sayac++)
		{
			cout << itr->veri << " ";
		}
		cout << endl;
	}

	int getOrtaDugum()
	{
		return ortaDugum->veri;
	}

	Dugum* getOrtaDugumAdres()
	{
		return ortaDugum;
	}

	~Liste()
	{
		ortaDugumDisindaSil();
		delete ortaDugum;
	}
};

class Listeler : public Dugum, protected Liste
{
private:
	Liste* listeler;
	DosyaOku* d1;

public:
	Listeler()
	{
		d1 = new DosyaOku();
		listeler = new Liste[d1->getSatirSayisi()];
		for (int i = 0; i < d1->getSatirSayisi(); i++)
		{
			listeler[i].Ekle(d1->getSayilar(), d1->getElemanSayisi(i + 1), i);
		}
	}

	void listelerYazdir()
	{
		for (int i = 0; i < d1->getSatirSayisi(); i++)
		{
			listeler[i].listeYazdir();
		}
	}

	int buyukIndex;
	int kucukIndex;

	Liste* ortaDugumKarsilastir()
	{
		int* ortaDugumler = new int[d1->getSatirSayisi()];
		for (int i = 0; i < d1->getSatirSayisi(); i++)
		{
			ortaDugumler[i] = listeler[i].getOrtaDugum();
		}
		Liste* BuyukveKucuk = new Liste[2];
		int buyuk = ortaDugumler[0];
		int kucuk = ortaDugumler[0];
		for (int i = 0; i < d1->getSatirSayisi(); i++)
		{
			if (buyuk < ortaDugumler[i])
				buyuk = ortaDugumler[i];

			if (kucuk > ortaDugumler[i])
				kucuk = ortaDugumler[i];
		}

		for (int i = 0; i < d1->getSatirSayisi(); i++)
		{
			if (buyuk == ortaDugumler[i]) buyukIndex = i;
			else if (kucuk == ortaDugumler[i]) kucukIndex = i;
		}
		delete[] ortaDugumler;
		BuyukveKucuk[0] = listeler[buyukIndex];
		BuyukveKucuk[1] = listeler[kucukIndex];
		return BuyukveKucuk;
	}

	void CaprazlaveYazdir()
	{
		Liste* yedekBuyuk = new Liste();
		Liste* yedekKucuk = new Liste();

		Liste* BuyukveKucuk = ortaDugumKarsilastir();
		Liste* Buyuk = &BuyukveKucuk[0];
		Liste* Kucuk = &BuyukveKucuk[1];

		yedekBuyuk = Buyuk->listeYedekle(yedekBuyuk, d1->getElemanSayisi(buyukIndex + 1));
		yedekKucuk = Kucuk->listeYedekle(yedekKucuk, d1->getElemanSayisi(kucukIndex + 1));

		Buyuk->ortaDugumDisindaSil();
		Kucuk->ortaDugumDisindaSil();

		Buyuk->yedekGuncelle(yedekKucuk, d1->getElemanSayisi(kucukIndex + 1));
		Kucuk->yedekGuncelle(yedekBuyuk, d1->getElemanSayisi(buyukIndex + 1));

		cout << "En Buyuk Liste Orta Dugum Adres: " << Buyuk->getOrtaDugumAdres(); cout << endl;
		cout << "En Buyuk Liste Degerler:" << endl; Buyuk->listeYazdir(); cout << endl << endl;
		cout << "En Kucuk Liste Orta Dugum Adres: " << Kucuk->getOrtaDugumAdres(); cout << endl;
		cout << "En Kucuk Liste Degerler:" << endl; Kucuk->listeYazdir(); cout << endl << endl;

		delete yedekBuyuk;
		delete yedekKucuk;
		delete Buyuk;
		delete Kucuk;
	}

	~Listeler()
	{
		delete d1;
		delete[] listeler;
	}
};

int main()
{
	Listeler* l1 = new Listeler();
	l1->CaprazlaveYazdir();
	delete l1;
}