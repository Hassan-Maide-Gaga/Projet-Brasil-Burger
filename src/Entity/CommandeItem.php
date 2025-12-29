<?php

namespace App\Entity;

use App\Repository\CommandeItemRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: CommandeItemRepository::class)]
class CommandeItem
{    
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(name: "type_item", enumType: TypeCommandeItem::class)]
    private ?TypeCommandeItem $type = null;

    #[ORM\Column(name: "item_id")]
    private ?int $produitId = null;

    #[ORM\Column]
    private ?int $quantite = null;

    #[ORM\Column(name: "prix_unitaire")]
    private ?float $prixUnitaire = null;

    #[ORM\ManyToOne(targetEntity: Commande::class, inversedBy: 'commandeItems')]
    #[ORM\JoinColumn(name: "commande_id", nullable: false)]
    private ?Commande $commande = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getType(): ?TypeCommandeItem
    {
        return $this->type;
    }

    public function setType(TypeCommandeItem $type): static
    {
        $this->type = $type;

        return $this;
    }

    public function getProduitId(): ?int
    {
        return $this->produitId;
    }

    public function setProduitId(int $produitId): static
    {
        $this->produitId = $produitId;

        return $this;
    }

    public function getQuantite(): ?int
    {
        return $this->quantite;
    }

    public function setQuantite(int $quantite): static
    {
        $this->quantite = $quantite;

        return $this;
    }

    public function getPrixUnitaire(): ?float
    {
        return $this->prixUnitaire;
    }

    public function setPrixUnitaire(float $prixUnitaire): static
    {
        $this->prixUnitaire = $prixUnitaire;

        return $this;
    }

    public function getCommande(): ?Commande
    {
        return $this->commande;
    }

    public function setCommande(?Commande $commande): static
    {
        $this->commande = $commande;

        return $this;
    }
}