library(ggplot2)

dir = 'img/'

x = seq(0,12,0.01)
y = sqrt(x)
png(paste(dir, 'square_root_function.png'), width=500, height=300)
ggplot(data.frame(x=x, y=y), aes(x=x, y=y)) + geom_line()
dev.off()

x = seq(-2,2,0.01)
y1 = 2^x
y2 = 0.5^x
df = data.frame(x=x,y1=y1,y2=y2)
names(df)[[2]] = 'a = 2'
names(df)[[3]] = 'a = 0.5'
df.m = melt(df, id.vars=c('x'))
names(df.m)[[2]] = 'Funktsioon'
names(df.m)[[3]] = 'y'
png(paste(dir, 'exponent_function.png'), width=500, height=500)
ggplot(df.m, aes(x=x, y=y)) + geom_line(aes(group=Funktsioon, color=Funktsioon))
dev.off()

x = seq(0,500,0.1)
y = log(x)
png(paste(dir, 'logarithm_function.png'), width=500, height=300)
ggplot(data.frame(x=x, y=y), aes(x=x, y=y)) + geom_line()
dev.off()

x = seq(-2*pi, 2*pi, 0.1)
y = sin(x)
png(paste(dir, 'sin_function.png'), width=500, height=300)
ggplot(data.frame(x=x, y=y), aes(x=x, y=y)) + geom_line()
dev.off()

x = seq(-2*pi, 2*pi, 0.1)
y = cos(x)
png(paste(dir, 'cos_function.png'), width=500, height=300)
ggplot(data.frame(x=x, y=y), aes(x=x, y=y)) + geom_line()
dev.off()

x = seq(-pi*1.5, pi*1.5, 0.0001)
y = tan(x)
png(paste(dir, 'tan_function.png'), width=500, height=300)
ggplot(data.frame(x=x, y=y), aes(x=x, y=y)) + geom_line() + ylim(-5, 5)
dev.off()

