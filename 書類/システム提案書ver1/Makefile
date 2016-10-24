all: texdvi dvipdf clean

texdvi: システム提案書ver1.tex
	platex $+
	platex $+

dvipdf: システム提案書ver1.dvi
	dvipdfmx システム提案書ver1.dvi

open: システム提案書ver1.pdf
	open システム提案書ver1.pdf

clean:
	rm -f *~ *.dvi *.aux *.log
