package com.galaxy.backend.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Producao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate data;
    private int lancamentos;
    private BigDecimal premioLiquido;
    private BigDecimal creditos;
    private BigDecimal estornos;
    private BigDecimal saldo;

    @ManyToOne
    @JoinColumn(name="corretora_id", nullable=false)
    private Corretora corretora;

    @JsonManagedReference
    @OneToMany(mappedBy = "producao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProducaoDetail> producoes =  new ArrayList<>();

    public Producao() {
    }

    public Producao(Long id, LocalDate data, Integer lancamentos, BigDecimal premioLiquido, BigDecimal creditos, BigDecimal estornos, BigDecimal saldo, Corretora corretora, List<ProducaoDetail> producoes) {
        this.id = id;
        this.data = data;
        this.lancamentos = lancamentos;
        this.premioLiquido = premioLiquido;
        this.creditos = creditos;
        this.estornos = estornos;
        this.saldo = saldo;
        this.corretora = corretora;
        this.producoes = producoes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public int getLancamentos() {
        return lancamentos;
    }

    public void setLancamentos(int lancamentos) {
        this.lancamentos = lancamentos;
    }

    public BigDecimal getPremioLiquido() {
        return premioLiquido;
    }

    public void setPremioLiquido(BigDecimal premioLiquido) {
        this.premioLiquido = premioLiquido;
    }

    public BigDecimal getCreditos() {
        return creditos;
    }

    public void setCreditos(BigDecimal creditos) {
        this.creditos = creditos;
    }

    public BigDecimal getEstornos() {
        return estornos;
    }

    public void setEstornos(BigDecimal estornos) {
        this.estornos = estornos;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Corretora getCorretora() {
        return corretora;
    }

    public void setCorretora(Corretora corretora) {
        this.corretora = corretora;
    }

    public List<ProducaoDetail> getProducoes() {
        return producoes;
    }

    public void setProducoes(List<ProducaoDetail> producoes) {
        this.producoes = producoes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producao producao = (Producao) o;
        return Objects.equals(id, producao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Producao{" +
                "id=" + id +
                ", data=" + data +
                ", lancamentos=" + lancamentos +
                ", premioLiquido=" + premioLiquido +
                ", creditos=" + creditos +
                ", estornos=" + estornos +
                ", saldo=" + saldo +
                ", corretora=" + corretora +
                ", producoes=" + producoes +
                '}';
    }
}
