import { create, updatePrice, updateDescription, deleteProduct } from '../api/productsApi'
import type { Command } from './Command'

export class CreateProductCommand implements Command {
  constructor(
    private name: string,
    private description: string,
    private price: number,
    private shopId: number,
    private userEmail: string | undefined,
    private onSuccess: () => void
  ) {}

  async execute(): Promise<void> {
    await create(this.name, this.description, this.price, this.shopId, this.userEmail)
    this.onSuccess()
  }
}

export class UpdatePriceCommand implements Command {
  constructor(
    private id: number,
    private price: number,
    private userEmail: string | undefined,
    private onSuccess: () => void
  ) {}

  async execute(): Promise<void> {
    await updatePrice(this.id, this.price, this.userEmail)
    this.onSuccess()
  }
}

export class UpdateDescriptionCommand implements Command {
  constructor(
    private id: number,
    private description: string,
    private userEmail: string | undefined,
    private onSuccess: () => void
  ) {}

  async execute(): Promise<void> {
    await updateDescription(this.id, this.description, this.userEmail)
    this.onSuccess()
  }
}

export class DeleteProductCommand implements Command {
  constructor(
    private id: number,
    private onSuccess: () => void
  ) {}

  async execute(): Promise<void> {
    await deleteProduct(this.id)
    this.onSuccess()
  }
}
