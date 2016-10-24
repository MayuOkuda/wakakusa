all: texdvi dvipdf clean

texdvi: システム提案書ver2.tex
	platex $+
	platex $+

dvipdf: システム提案書ver2.dvi
	dvipdfmx システム提案書ver2.dvi

open: システム提案書ver2.pdf
	open システム提案書ver2.pdf

clean:
	rm -f *~ *.dvi *.aux *.log
