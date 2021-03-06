CREATE DATABASE DBNextSport2014277
go
-- DROP DATABASE DBNextSport2014277
USE DBNextSport2014277
go
/*
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
   +                       Categorias                                 +
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
*/
CREATE TABLE Categorias
	(
		codigoCategoria INT IDENTITY(1,1) PRIMARY KEY,
		descripcion VARCHAR(20)
	)
go

CREATE PROCEDURE sp_AgregarCategoria @descripcion VARCHAR(20)
as
	INSERT INTO Categorias VALUES (@descripcion)
go

CREATE PROCEDURE sp_EliminarCategoria @codigoCategoria INT
as
	DELETE FROM Categorias WHERE codigoCategoria = @codigoCategoria
go

CREATE PROCEDURE sp_ListarCategorias
as
	SELECT Categorias.codigoCategoria, Categorias.descripcion FROM Categorias
go

CREATE PROCEDURE sp_BuscarCategoria @codigoCategoria INT
as
	SELECT Categorias.codigoCategoria, Categorias.descripcion FROM Categorias WHERE codigoCategoria = @codigoCategoria
go

CREATE PROCEDURE sp_ActualizarCategoria @codigoCategoria INT, @descripcion VARCHAR(20)
as
	UPDATE Categorias SET descripcion = @descripcion WHERE codigoCategoria = @codigoCategoria;
go
/*
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
   +                           Tallas                                 +
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
*/
CREATE TABLE Tallas
	(
		codigoTalla INT IDENTITY(1,1) PRIMARY KEY,
		descripcion VARCHAR(20)
	)
go

CREATE PROCEDURE sp_AgregarTalla @descripcion VARCHAR(20)
as
	INSERT INTO Tallas VALUES (@descripcion)
go

CREATE PROCEDURE sp_EliminarTalla @codigoTalla INT
as
	DELETE FROM Tallas WHERE codigoTalla = @codigoTalla
go

CREATE PROCEDURE sp_ListarTallas
as
	SELECT Tallas.codigoTalla, Tallas.descripcion FROM Tallas
go

CREATE PROCEDURE sp_BuscarTalla @codigoTalla INT
as
	SELECT Tallas.codigoTalla, Tallas.descripcion FROM Tallas WHERE codigoTalla = @codigoTalla
go

CREATE PROCEDURE sp_ActualizarTalla @codigoTalla INT, @descripcion VARCHAR(20)
as
	UPDATE Tallas SET descripcion = @descripcion WHERE codigoTalla = @codigoTalla
go
/*
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
   +                           Marcas                                 +
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
*/
CREATE TABLE Marcas
	(
		codigoMarca INT IDENTITY(1,1) PRIMARY KEY,
		descripcion VARCHAR(20)
	)
go

CREATE PROCEDURE sp_AgregarMarca @descripcion VARCHAR(20)
as
	INSERT INTO Marcas VALUES (@descripcion)

go

CREATE PROCEDURE sp_EliminarMarca @codigoMarca INT
as
	DELETE FROM Marcas WHERE codigoMarca = @codigoMarca
go

CREATE PROCEDURE sp_ListarMarcas
as
	SELECT Marcas.codigoMarca, Marcas.descripcion FROM Marcas
go

CREATE PROCEDURE sp_BuscarMarca @codigoMarca INT
as
	SELECT Marcas.codigoMarca, Marcas.descripcion FROM Marcas WHERE codigoMarca = @codigoMarca
go

CREATE PROCEDURE sp_ActualizarMarca @codigoMarca INT, @descripcion VARCHAR(20)
as
	UPDATE Marcas SET descripcion = @descripcion WHERE codigoMarca = @codigoMarca
go
/*
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
   +                          Productos                               +
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
*/
CREATE TABLE Productos
	(
		codigoProducto INT IDENTITY(1,1) PRIMARY KEY,
		descripcion VARCHAR(20) NOT NULL,
		existencia INT DEFAULT(0),
		imagen VARCHAR(120),
		precioUnitario DECIMAL(10,2) DEFAULT (0.00),
		precioDocena DECIMAL(10,2) DEFAULT (0.00),
		precioMayor DECIMAL(10,2) DEFAULT (0.00),
		codigoCategoria INT NOT NULL,
		codigoMarca INT NOT NULL,
		codigoTalla INT NOT NULL,
		/* Foreign keys */
		CONSTRAINT FK_Productos_Categorias FOREIGN KEY (codigoCategoria)
			REFERENCES Categorias(codigoCategoria),
		CONSTRAINT FK_Productos_Marcas FOREIGN KEY (codigoMarca)
			REFERENCES Marcas(codigoMarca),
		CONSTRAINT FK_Productos_Tallas FOREIGN KEY (codigoTalla)
			REFERENCES Tallas(codigoTalla)
	)
go

CREATE PROCEDURE sp_AgregarProducto
	@descripcion VARCHAR(30),
	@codigoCategoria INT,
	@codigoMarca INT,
	@codigoTalla INT,
	@imagen VARCHAR(120)
as
	INSERT INTO Productos (codigoCategoria, codigoMarca, codigoTalla, descripcion, imagen)
		VALUES (@codigoCategoria, @codigoMarca, @codigoTalla, @descripcion, @imagen)
go

CREATE PROCEDURE sp_EliminarProducto @codigoProducto INT
as
	DELETE FROM Productos WHERE codigoProducto = @codigoProducto
go

CREATE PROCEDURE sp_ListarProductos
as
	SELECT
		Productos.codigoProducto,
		Productos.descripcion,
		Productos.existencia,
		Productos.codigoCategoria,
		Productos.codigoMarca,
		Productos.codigoTalla,
		Productos.imagen,
		Productos.precioUnitario,
		Productos.precioDocena,
		Productos.precioMayor,
		Productos.codigoCategoria,
		Categorias.descripcion as Categoria,
		Productos.codigoMarca,
		Marcas.descripcion as Marca,
		Productos.codigoTalla,
		Tallas.descripcion as Talla
	FROM
		Productos
	INNER JOIN Categorias on Productos.codigoCategoria = Categorias.codigoCategoria
	INNER JOIN Marcas on Productos.codigoMarca = Marcas.codigoMarca
	INNER JOIN Tallas on Productos.codigoTalla = Tallas.codigoTalla
go
CREATE PROCEDURE sp_BuscarProducto @codigoProducto INT
as
	SELECT
		Productos.codigoProducto,
		Productos.descripcion,
		Productos.existencia,
		Productos.codigoCategoria,
		Productos.codigoMarca,
		Productos.codigoTalla,
		Productos.imagen,
		Productos.precioUnitario,
		Productos.precioDocena,
		Productos.precioMayor,
		Productos.codigoCategoria,
		Categorias.descripcion as Categoria,
		Productos.codigoMarca,
		Marcas.descripcion as Marca,
		Productos.codigoTalla,
		Tallas.descripcion as Talla
	FROM
		Productos
	INNER JOIN Categorias on Productos.codigoCategoria = Categorias.codigoCategoria
	INNER JOIN Marcas on Productos.codigoMarca = Marcas.codigoMarca
	INNER JOIN Tallas on Productos.codigoTalla = Tallas.codigoTalla
	WHERE
		codigoProducto = @codigoProducto
go

CREATE PROCEDURE sp_ActualizarProducto
	@codigoProducto INT,
	@descripcion VARCHAR(30),
	@codigoCategoria INT,
	@codigoMarca INT,
	@codigoTalla INT,
	@imagen VARCHAR(120)
as
	UPDATE Productos
		SET codigoCategoria = @codigoCategoria, codigoMarca = @codigoMarca, codigoTalla = @codigoTalla, descripcion = @descripcion, imagen = @imagen
		WHERE codigoProducto = @codigoProducto
go
/*
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
   +                         Proveedores                              +
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
*/
CREATE TABLE Proveedores
	(
		codigoProveedor INT IDENTITY(1,1) PRIMARY KEY,
		contactoPrincipal VARCHAR(25),
		razonSocial VARCHAR(25),
		nit VARCHAR(12),
		paginaWeb VARCHAR(30),
		direccion VARCHAR(30)
	)
go

CREATE PROCEDURE sp_AgregarProveedor
	@contactoPrincipal VARCHAR(30),
	@razonSocial VARCHAR(30),
	@nit VARCHAR(12),
	@paginaWeb VARCHAR(30),
	@direccion VARCHAR(30)
as
	INSERT INTO Proveedores (direccion, razonSocial, contactoPrincipal, paginaWeb, nit)
		VALUES (@direccion, @razonSocial, @contactoPrincipal, @paginaWeb, @nit)
go

CREATE PROCEDURE sp_EliminarProveedor @codigoProveedor INT
as
	DELETE FROM Proveedores WHERE codigoProveedor = @codigoProveedor
go

CREATE PROCEDURE sp_ListarProveedores
as
	SELECT
		Proveedores.codigoProveedor,
		Proveedores.contactoPrincipal,
		Proveedores.razonSocial,
		Proveedores.nit,
		Proveedores.paginaWeb,
		Proveedores.direccion
	FROM
		Proveedores
go

CREATE PROCEDURE sp_BuscarProveedor
	@codigoProveedor INT
as
	SELECT
		Proveedores.codigoProveedor,
		Proveedores.contactoPrincipal,
		Proveedores.razonSocial,
		Proveedores.nit,
		Proveedores.paginaWeb,
		Proveedores.direccion
	FROM
		Proveedores
	WHERE codigoProveedor = @codigoProveedor
go

CREATE PROCEDURE sp_ActualizarProveedor
	@codigoProveedor INT,
	@contactoPrincipal VARCHAR(30),
	@direccion VARCHAR(30),
	@razonSocial VARCHAR(30),
	@paginaWeb VARCHAR(30),
	@nit VARCHAR(12)
as
	UPDATE Proveedores
		SET direccion = @direccion, razonSocial = @razonSocial, contactoPrincipal = @contactoPrincipal, paginaWeb = @paginaWeb, nit = @nit
		WHERE codigoProveedor = @codigoProveedor
go

/*
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
   +                       EmailProveedor                             +
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
*/
CREATE TABLE EmailProveedor
	(
		codigoEmailProveedor INT IDENTITY(1,1) PRIMARY KEY,
		email VARCHAR(30),
		descripcion VARCHAR(30),
		codigoProveedor INT NOT NULL,
		/* Foreign Key */
		CONSTRAINT FK_EmailProveedor_Proveedores FOREIGN KEY (codigoProveedor)
			REFERENCES Proveedores(codigoProveedor)
	)
go

CREATE PROCEDURE sp_AgregarEmailProveedor
	@email VARCHAR(30),
	@descripcion VARCHAR(25),
	@codigoProveedor INT
as
	INSERT INTO EmailProveedor (email, descripcion, codigoProveedor)
		VALUES (@email, @descripcion, @codigoProveedor)

go

CREATE PROCEDURE sp_EliminarEmailProveedor @codigoEmailProveedor INT
as
	DELETE FROM EmailProveedor WHERE codigoEmailProveedor = @codigoEmailProveedor
go

CREATE PROCEDURE sp_ListarEmailProveedor
as
	SELECT
		EmailProveedor.codigoEmailProveedor,
		EmailProveedor.codigoProveedor,
		EmailProveedor.descripcion,
		EmailProveedor.email
	FROM EmailProveedor
go

CREATE PROCEDURE sp_BuscarEmailProveedor @codigoEmailProveedor INT
as
	SELECT
		EmailProveedor.codigoEmailProveedor,
		EmailProveedor.codigoProveedor,
		EmailProveedor.descripcion,
		EmailProveedor.email
	FROM EmailProveedor
	WHERE codigoEmailProveedor = @codigoEmailProveedor
go

CREATE PROCEDURE sp_ActualizarEmailProveedor
	@codigoEmailProveedor INT,
	@email VARCHAR(30),
	@descripcion VARCHAR(25),
	@codigoProveedor INT
as
	UPDATE EmailProveedor SET email = @email, descripcion = @descripcion, codigoProveedor = @codigoProveedor
		WHERE codigoEmailProveedor = @codigoEmailProveedor
go

/*
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
   +                      TelefonoProveedor                           +
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
*/
CREATE TABLE TelefonoProveedor
	(
		codigoTelefonoProveedor INT IDENTITY(1,1),
		descripcion VARCHAR(15) NOT NULL,
		numero VARCHAR(15) NOT NULL,
		codigoProveedor INT NOT NULL,
		/* Foreign Key */
		CONSTRAINT FK_TelefonoProveedor_Proveedores FOREIGN KEY (codigoProveedor)
			REFERENCES Proveedores(codigoProveedor)
	)
go

CREATE PROCEDURE sp_AgregarTelefonoProveedor 
	@descripcion VARCHAR(15),
	@numero VARCHAR(15),
	@codigoProveedor INT
as
	INSERT INTO TelefonoProveedor (descripcion, numero, codigoProveedor)
		VALUES (@descripcion, @numero, @codigoProveedor)
go

CREATE PROCEDURE sp_EliminarTelefonoProveedor @codigoTelefonoProveedor INT
as
	DELETE FROM TelefonoProveedor WHERE codigoTelefonoProveedor = @codigoTelefonoProveedor
go

CREATE PROCEDURE sp_ListarTelefonosProveedores
as
	SELECT
		TelefonoProveedor.codigoTelefonoProveedor,
		TelefonoProveedor.descripcion,
		TelefonoProveedor.numero,
		TelefonoProveedor.codigoProveedor
	FROM
		TelefonoProveedor
go

CREATE PROCEDURE sp_BuscarTelefonoProveedor @codigoTelefonoProveedor INT
as
	SELECT
		TelefonoProveedor.codigoTelefonoProveedor,
		TelefonoProveedor.descripcion,
		TelefonoProveedor.numero,
		TelefonoProveedor.codigoProveedor
	FROM
		TelefonoProveedor
	WHERE
		codigoTelefonoProveedor = @codigoTelefonoProveedor
go

CREATE PROCEDURE sp_ActualizarTelefonoProveedor
	@codigoTelefonoProveedor INT,
	@descripcion VARCHAR(15),
	@numero VARCHAR(15),
	@codigoProveedor INT
as
	UPDATE TelefonoProveedor SET descripcion = @descripcion, numero = @numero, codigoProveedor = @codigoProveedor
		WHERE codigoTelefonoProveedor = @codigoTelefonoProveedor
go
/*
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
   +                           Compras                                +
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
*/
CREATE TABLE Compras
	(
		numeroDocumento INT PRIMARY KEY,
		descripcion VARCHAR(30) NOT NULL,
		total FLOAT DEFAULT(0.0),
		fecha DATE NOT NULL,
		codigoProveedor INT NOT NULL,
		/* Foreign Key */
		CONSTRAINT FK_Compras_Proveedores FOREIGN KEY (codigoProveedor)
			REFERENCES Proveedores(codigoProveedor)
	)
go

CREATE PROCEDURE sp_AgregarCompra
	@numeroDocumento INT,
	@descripcion VARCHAR(30),
	@fecha DATE,
	@codigoProveedor INT
as
	INSERT INTO Compras (numeroDocumento, descripcion, fecha, codigoProveedor)
		VALUES (@numeroDocumento, @descripcion, @fecha, @codigoProveedor)
go

CREATE PROCEDURE sp_EliminarCompra @numeroDocumento INT
as
	DELETE FROM Compras WHERE numeroDocumento = @numeroDocumento
go

CREATE PROCEDURE sp_ListarCompras
as
	SELECT
		Compras.numeroDocumento,
		Compras.descripcion,
		Compras.total,
		Compras.fecha,
		Compras.codigoProveedor,
		Proveedores.contactoPrincipal,
		Proveedores.razonSocial,
		Proveedores.nit,
		Proveedores.paginaWeb,
		Proveedores.direccion
	FROM Compras
	INNER JOIN Proveedores on Compras.codigoProveedor = Proveedores.codigoProveedor
go

CREATE PROCEDURE sp_BuscarCompra @numeroDocumento INT	
as
	SELECT
		Compras.numeroDocumento,
		Compras.descripcion,
		Compras.codigoProveedor,
		Compras.fecha,
		Compras.total,
		Proveedores.razonSocial
	FROM Compras
	INNER JOIN Proveedores on Compras.codigoProveedor = Proveedores.codigoProveedor
	WHERE numeroDocumento = @numeroDocumento
go

CREATE PROCEDURE sp_ActualizarCompra
	@numeroDocumento INT,
	@descripcion VARCHAR(30),
	@fecha DATE,
	@codigoProveedor INT
as
	UPDATE Compras
		SET  descripcion = @descripcion, fecha = @fecha, codigoProveedor = @codigoProveedor
		WHERE numeroDocumento = @numeroDocumento
go
/*
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
   +                        DetalleCompra                             +
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
*/
CREATE TABLE DetalleCompra
	(
		codigoDetalleCompra INT IDENTITY(1,1) PRIMARY KEY,
		cantidad INT NOT NULL,
		costoUnitario INT NOT NULL,
		subtotal FLOAT,
		codigoProducto INT NOT NULL,
		numeroDocumento INT NOT NULL,
		/* Foreign Keys */
		CONSTRAINT FK_DetalleCompra_Productos FOREIGN KEY (codigoProducto)
			REFERENCES Productos(codigoProducto),
		CONSTRAINT FK_DetalleCompra_Compras FOREIGN KEY (numeroDocumento)
			REFERENCES Compras(numeroDocumento)
	)
go

CREATE PROCEDURE sp_AgregarDetalleCompra
	@cantidad INT,
	@costoUnitario INT,
	@codigoProducto INT,
	@numeroDocumento INT
as
	INSERT INTO DetalleCompra (cantidad, costoUnitario, subtotal, codigoProducto, numeroDocumento)
		VALUES (@cantidad, @costoUnitario, (@cantidad*@costoUnitario), @codigoProducto, @numeroDocumento)
go

CREATE PROCEDURE sp_EliminarDetalleCompra @codigoDetalleCompra INT
as
	DELETE FROM DetalleCompra WHERE codigoDetalleCompra = @codigoDetalleCompra
go

CREATE PROCEDURE sp_ListarDetalleCompra
as
	SELECT
		DetalleCompra.codigoDetalleCompra,
		DetalleCompra.numeroDocumento,
		DetalleCompra.codigoProducto,
		DetalleCompra.cantidad,
		DetalleCompra.costoUnitario,
		DetalleCompra.subtotal
	FROM DetalleCompra
go

CREATE PROCEDURE sp_BuscarDetalleCompra 
	@numeroDocumento INT
as
	SELECT TOP 1
		DetalleCompra.codigoDetalleCompra,
		DetalleCompra.numeroDocumento,
		DetalleCompra.codigoProducto,
		Productos.descripcion,
		DetalleCompra.cantidad,
		DetalleCompra.costoUnitario,
		DetalleCompra.subtotal
	FROM DetalleCompra
	INNER JOIN Productos on DetalleCompra.codigoProducto = Productos.codigoProducto
	WHERE numeroDocumento = @numeroDocumento
go

CREATE PROCEDURE sp_ActualizarDetalleCompra
	@codigoDetalleCompra INT,
	@cantidad INT,
	@costoUnitario INT,
	@subtotal FLOAT,
	@codigoProducto INT,
	@numeroDocumento INT
as
	UPDATE DetalleCompra
		SET cantidad = @cantidad, costoUnitario = @costoUnitario, subtotal = @subtotal, codigoProducto = @codigoProducto, numeroDocumento = @numeroDocumento
		WHERE codigoDetalleCompra = @codigoDetalleCompra
go

CREATE TRIGGER TriggerDetalleCompra
ON DetalleCompra
AFTER INSERT
AS
	UPDATE Compras
	SET total = (SELECT subtotal FROM inserted)
	WHERE numeroDocumento = (SELECT numeroDocumento FROM inserted)
go

-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!




/*
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
   +                           Clientes                               +
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
*/
CREATE TABLE Clientes
	(
		codigoCliente INT IDENTITY(1,1) PRIMARY KEY,
		nombre VARCHAR(25) NOT NULL,
		direccion VARCHAR(25),
		nit VARCHAR(25) NOT NULL
	)
go

CREATE PROCEDURE sp_AgregarCliente @nombre VARCHAR(25), @direccion VARCHAR(25), @nit VARCHAR(50)
as
	INSERT INTO Clientes (nombre, direccion, nit) VALUES (@nombre, @direccion, @nit)
go

CREATE PROCEDURE sp_EliminarCliente @codigoCliente INT
as
	DELETE FROM Clientes WHERE codigoCliente = @codigoCliente
go

CREATE PROCEDURE sp_ListarClientes
as
	SELECT
		Clientes.codigoCliente,
		Clientes.nombre,
		Clientes.direccion,
		Clientes.nit
	FROM
		Clientes
go

CREATE PROCEDURE sp_BuscarCliente @codigoCliente INT
as
	SELECT
		Clientes.codigoCliente,
		Clientes.nombre,
		Clientes.direccion,
		Clientes.nit
	FROM
		Clientes
	WHERE codigoCliente = @codigoCliente
go

CREATE PROCEDURE sp_ActualizarCliente
	@codigoCliente INT,
	@nombre VARCHAR(25),
	@direccion VARCHAR(25),
	@nit VARCHAR(20)
as
	UPDATE Clientes SET nombre = @nombre, direccion = @direccion, nit = @nit
		WHERE codigoCliente = @codigoCliente
go

/*
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
   +                        TelefonoCliente                           +
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
*/
CREATE TABLE TelefonoCliente
	(
		codigoTelefonoCliente INT IDENTITY(1,1) PRIMARY KEY,
		numero VARCHAR(12) NOT NULL,
		descripcion VARCHAR(25),
		codigoCliente INT NOT NULL,
		/* Foreign Key */
		CONSTRAINT FK_TelefonoCliente_Clientes FOREIGN KEY (codigoCliente)
			REFERENCES Clientes(codigoCliente)
	)
go
CREATE PROCEDURE sp_AgregarTelefonoCliente
	@descripcion VARCHAR(25),
	@numero VARCHAR(12),
	@codigoCliente INT
as
	INSERT INTO TelefonoCliente (numero, descripcion, codigoCliente)
		VALUES (@numero, @descripcion, @codigoCliente)
go

CREATE PROCEDURE sp_EliminarTelefonoCliente  @codigoTelefonoCliente INT
as
	DELETE FROM TelefonoCliente WHERE codigoTelefonoCliente = @codigoTelefonoCliente
go

CREATE PROCEDURE sp_ListarTelefonosClientes
as
	SELECT
		TelefonoCliente.codigoTelefonoCliente,
		TelefonoCliente.numero,
		TelefonoCliente.descripcion,
		TelefonoCliente.codigoCliente
	FROM
		TelefonoCliente
go

CREATE PROCEDURE sp_BuscarTelefonoCliente @codigoTelefonoCliente INT
as
	SELECT
		TelefonoCliente.codigoTelefonoCliente,
		TelefonoCliente.numero,
		TelefonoCliente.descripcion,
		TelefonoCliente.codigoCliente
	FROM
		TelefonoCliente
	WHERE
		codigoTelefonoCliente = @codigoTelefonoCliente
go

CREATE PROCEDURE sp_ActualizarTelefonoCliente
	@codigoTelefonoCliente INT,
	@numero VARCHAR(12),
	@descripcion VARCHAR(25),
	@codigoCliente INT
as
	UPDATE TelefonoCliente SET numero = @numero, descripcion = @descripcion, codigoCliente = @codigoCliente
		WHERE codigoTelefonoCliente = @codigoTelefonoCliente
go
/*
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
   +                          EmailCliente                            +
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
*/
CREATE TABLE EmailCliente
	(
		codigoEmailCliente INT IDENTITY(1,1) PRIMARY KEY,
		descripcion VARCHAR(25),
		email VARCHAR(35) NOT NULL,
		codigoCliente INT NOT NULL,
		/* Foreign Key */
		CONSTRAINT FK_EmailCliente_Clientes FOREIGN KEY (codigoCliente)
			REFERENCES Clientes(codigoCliente)
	)
go

CREATE PROCEDURE sp_AgregarEmailCliente
	@descripcion VARCHAR(25),
	@email VARCHAR(35),
	@codigocliente INT
as
	INSERT INTO EmailCliente (descripcion, email, codigoCliente)
		VALUES (@descripcion, @email, @codigoCliente)
go

CREATE PROCEDURE sp_EliminarEmailCliente @codigoEmailCliente INT
as
	DELETE FROM EmailCliente WHERE codigoEmailCliente = @codigoEmailCliente
go

CREATE PROCEDURE sp_ListarEmailCliente
as
	SELECT
		EmailCliente.codigoEmailCliente,
		EmailCliente.descripcion,
		EmailCliente.email,
		EmailCliente.codigoCliente
	FROM
		EmailCliente
go

CREATE PROCEDURE sp_BuscarEmailCliente @codigoEmailCliente INT
as
	SELECT
		EmailCliente.codigoEmailCliente,
		EmailCliente.descripcion,
		EmailCliente.email,
		EmailCliente.codigoCliente
	FROM
		EmailCliente
	WHERE
		codigoEmailCliente = @codigoEmailCliente
go

CREATE PROCEDURE sp_ActualizarEmailCliente
	@codigoEmailCliente INT,
	@descripcion VARCHAR(25),
	@email VARCHAR(35),
	@codigoCliente INT
as
	UPDATE EmailCliente SET descripcion = @descripcion, email = @email, codigoCliente = @codigoCliente
		WHERE codigoEmailCliente = @codigoEmailCliente
go
/*
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
   +                           Facturas                               +
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
*/
CREATE TABLE Facturas
	(
		numeroFactura INT IDENTITY(1,1) PRIMARY KEY,
		estado VARCHAR(25) NOT NULL,
		nit VARCHAR(25) NOT NULL,
		total FLOAT DEFAULT(0.00),
		fecha DATE NOT NULL,
		codigoCliente INT NOT NULL
		CONSTRAINT FK_Facturas_Clientes FOREIGN KEY (codigoCliente)
			REFERENCES Clientes(codigoCliente)
	)
go

CREATE PROCEDURE sp_AgregarFactura
	@estado VARCHAR(25),
	@nit VARCHAR(25),
	@fecha DATE,
	@codigoCliente INT
as
	INSERT INTO Facturas (estado, nit, fecha, codigoCliente)
		VALUES (@estado, @nit, @fecha, @codigoCliente)
go

CREATE PROCEDURE sp_EliminarFactura @numeroFactura INT
as
	DELETE FROM Facturas WHERE numeroFactura = @numeroFactura
go

CREATE PROCEDURE sp_ListarFacturas
as
	SELECT
		Facturas.numeroFactura,
		Facturas.estado,
		Facturas.nit,
		Facturas.total,
		Facturas.fecha,
		Facturas.codigoCliente
	FROM
		Facturas
go

CREATE PROCEDURE sp_BuscarFacturas @numeroFactura INT
as
	SELECT
		Facturas.numeroFactura,
		Facturas.estado,
		Facturas.nit,
		Facturas.total,
		Facturas.fecha,
		Facturas.codigoCliente,
		Clientes.nombre
	FROM
		Facturas
	INNER JOIN Clientes on Facturas.codigoCliente = Clientes.codigoCliente
	WHERE
		numeroFactura = @numeroFactura
go

CREATE PROCEDURE sp_ActualizarFactura
	@numeroFactura INT,
	@estado VARCHAR(25),
	@nit VARCHAR(25),
	@fecha DATE,
	@codigoCliente INT
as
	UPDATE Facturas SET estado = @estado, nit = @nit, fecha = @fecha, codigoCliente = @codigoCliente
		WHERE numeroFactura = @numeroFactura
go

/*
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
   +                           DetalleFactura                         +
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
*/
CREATE TABLE DetalleFactura
	(
		codigoDetalleFactura INT IDENTITY(1,1),
		cantidad INT NOT NULL,
		precio FLOAT DEFAULT(0.0),
		codigoProducto INT NOT NULL,
		numeroFactura INT NOT NULL,
		CONSTRAINT FK_DetalleFactura_Productos FOREIGN KEY (codigoProducto)
			REFERENCES Productos(codigoProducto),
		CONSTRAINT FK_DetalleFactura_Facturas FOREIGN KEY (numeroFactura)
			REFERENCES Facturas(numeroFactura)
	)
go

CREATE PROCEDURE sp_AgregarDetalleFactura
	@cantidad INT,
	@codigoProducto INT,
	@numeroFactura INT
as
	INSERT INTO DetalleFactura (cantidad, codigoProducto, numeroFactura)
		VALUES (@cantidad, @codigoProducto, @numeroFactura)
go

CREATE PROCEDURE sp_ElimnarDetalleFactura @codigoDetalleFactura INT
as
	DELETE FROM DetalleFactura WHERE codigoDetalleFactura = @codigoDetalleFactura
go

CREATE PROCEDURE sp_ListarDetalleFacturas
as
	SELECT
		DetalleFactura.codigoDetalleFactura,
		DetalleFactura.cantidad, 
		DetalleFactura.precio,
		DetalleFactura.codigoProducto,
		DetalleFactura.numeroFactura
	FROM
		DetalleFactura
go

CREATE PROCEDURE sp_BuscarDetalleFactura @numeroFactura INT
as
	SELECT TOP 1
		DetalleFactura.codigoDetalleFactura,
		DetalleFactura.cantidad, 
		DetalleFactura.precio,
		DetalleFactura.codigoProducto,
		Productos.descripcion,
		DetalleFactura.numeroFactura
	FROM
		DetalleFactura
	INNER JOIN Productos on DetalleFactura.codigoProducto = Productos.codigoProducto
	WHERE numeroFactura = @numeroFactura
go

CREATE PROCEDURE sp_ActualizarDetalleFactura
	@codigoDetalleFactura INT,
	@cantidad INT,
	@precio FLOAT,
	@codigoProducto INT,
	@numeroFactura INT
as
	UPDATE DetalleFactura SET cantidad = @cantidad, precio = @precio, codigoProducto = @codigoProducto, numeroFactura = @numeroFactura
		WHERE codigoDetalleFactura = @codigoDetalleFactura
go

--execute sp_AgregarDetalleFactura 12, 1, 1
CREATE TRIGGER TriggerDetalleFactura
ON DetalleFactura
AFTER INSERT
AS
BEGIN
	DECLARE @cantidad INT
	SET @cantidad = (SELECT TOP 1 cantidad FROM inserted)
	UPDATE Facturas 
	SET total =	( SELECT TOP 1 inserted.cantidad*DetalleCompra.costoUnitario*(SELECT dbo.Ganancia(@cantidad)) 
				FROM inserted INNER JOIN DetalleCompra ON DetalleCompra.codigoProducto = inserted.codigoProducto
					WHERE numeroFactura= (SELECT TOP 1 numeroFactura FROM inserted))
	WHERE numeroFactura = (SELECT TOP 1 numeroFactura FROM inserted)
END
go

CREATE FUNCTION dbo.Ganancia (@cantidad INT)
RETURNS FLOAT 
AS
BEGIN
	DECLARE @resultado FLOAT;
	IF @cantidad < 12
		SET @resultado = 0.35
	ELSE
	BEGIN
			IF @cantidad < 24
				SET @resultado = 0.25
			ELSE
				SET @resultado = 0.15
	END
	RETURN @resultado
END

go

/* Resumen General */
SELECT 
    CASE TYPE 
        WHEN 'U' 
            THEN 'Tables' 
        WHEN 'P'
            THEN 'Stored Procedures'
    END AS Objeto, 
    COUNT(*) AS Cantidad
FROM SYS.OBJECTS
WHERE TYPE IN ('U', 'P')
GROUP BY TYPE