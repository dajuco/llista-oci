const express = require("express");
const router = express.Router();

router.get("/", async (req, res) => {
    try {
        const db = req.app.locals.db;
        const categorias = await db
            .collection("categorias")
            .find({}, { projection: { _id: 0 } })
            .toArray();

        res.json(categorias);
    } catch (error) {
        res.status(500).json({ error: "Error obtenint categories" });
    }
});

router.get("/:id", async (req, res) => {
    try {
        const db = req.app.locals.db;
        const id = req.params.id;

        const categoria = await db
            .collection("categorias")
            .findOne({ id: id }, { projection: { _id: 0 } });

        if (!categoria) return res.status(404).json({ error: "Categoria no trobada" });

        res.json(categoria);
    } catch (error) {
        res.status(500).json({ error: "Error obtenint categoria" });
    }
});

router.post("/", async (req, res) => {
    try {
        const db = req.app.locals.db;
        const cat = req.body;

        if (!cat.id || !cat.nombre) {
            return res.status(400).json({ error: "Falten camps obligatoris" });
        }

        const existent = await db.collection("categorias").findOne({ id: cat.id });
        if (existent) return res.status(409).json({ error: "Ja existeix una categoria amb aquest id" });

        await db.collection("categorias").insertOne({
            id: cat.id,
            nombre: cat.nombre
        });

        res.status(201).json({ message: "Categoria creada correctament" });
    } catch (error) {
        res.status(500).json({ error: "Error creant categoria" });
    }
});

router.put("/:id", async (req, res) => {
    try {
        const db = req.app.locals.db;
        const id = req.params.id;
        const updates = req.body;

        const result = await db.collection("categorias").updateOne({ id: id }, { $set: updates });

        if (result.matchedCount === 0) return res.status(404).json({ error: "Categoria no trobada" });

        res.json({ message: "Categoria actualitzada" });
    } catch (error) {
        res.status(500).json({ error: "Error actualitzant categoria" });
    }
});

router.delete("/:id", async (req, res) => {
    try {
        const db = req.app.locals.db;
        const id = req.params.id;

        const result = await db.collection("categorias").deleteOne({ id: id });

        if (result.deletedCount === 0) return res.status(404).json({ error: "Categoria no trobada" });

        res.json({ message: "Categoria eliminada" });
    } catch (error) {
        res.status(500).json({ error: "Error eliminant categoria" });
    }
});

module.exports = router;

